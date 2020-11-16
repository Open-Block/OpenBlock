package org.block.project.panel.network;

import org.block.Blocks;
import org.block.network.ConnectionInfo;
import org.block.network.client.ClientConnection;
import org.block.network.common.Connection;
import org.block.network.common.event.NetEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.request.RequestJoinConnectionPacketBuilder;
import org.block.network.common.packets.request.RequestJoinEvent;
import org.block.network.common.packets.verify.VerifyConnectionEvent;
import org.block.network.common.packets.verify.VerifyConnectionPacketBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketPermission;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class NetworkClientPanel extends JPanel {

    public class OnListen implements NetworkListener {

        @NetEvent
        public void onRequest(RequestJoinEvent event){
            if(!event.getPacketValue().hasAccepted()) {
                NetworkClientPanel.this.log.append("\n\tHost denied your request");
                return;
            }
            NetworkClientPanel.this.log.append("\n\tHost accepted your request");

        }
    }

    public class LanFinder implements Runnable {

        public class OnReceive implements NetworkListener {

            private final String id;

            public OnReceive(String id){
                this.id = id;
            }

            @NetEvent
            public void onVerify(VerifyConnectionEvent event){
                String id = event.getConnection().getTargetSocket().getInetAddress().getHostAddress() + " (" + event.getValue().getId().get() + ")";
                ListModel<String> lModel = NetworkClientPanel.this.lanConnection.getModel();
                DefaultListModel<String> model = (DefaultListModel<String>)lModel;
                Enumeration<String> elements = model.elements();
                int A = 0;
                while(elements.hasMoreElements()){
                    A++;
                    String element = elements.nextElement();
                    if(!element.equals(this.id)){
                        continue;
                    }
                    break;
                }
                model.setElementAt(id,A - 1);
            }

        }

        @Override
        public void run() {
            try {
                Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
                while (ni.hasMoreElements()) {
                    NetworkInterface network = ni.nextElement();
                    if(network.isLoopback()){
                        continue;
                    }
                    if(!network.isUp()){
                        continue;
                    }
                    if(network.isVirtual()){
                        continue;
                    }
                    if(network.getInterfaceAddresses().isEmpty()){
                        continue;
                    }
                    String address = network.getInterfaceAddresses().get(0).getAddress().getHostAddress();
                    if(address.split("\\.").length != 4){
                        continue;
                    }
                    String[] splitAddress = address.split("\\.");
                    address = address.substring(0, address.length() - splitAddress[3].length());
                    for (int A = 0; A <= 256; A++) {
                        final String finalAddress = address + A;
                        new Thread(() -> {
                            try {
                                Socket socket = new Socket(finalAddress, Integer.parseInt(NetworkClientPanel.this.portField.getText()));
                                final ClientConnection connection = new ClientConnection(socket);
                                new Thread(connection::connect).start();
                                ListModel<String> lModel = NetworkClientPanel.this.lanConnection.getModel();
                                DefaultListModel<String> model = (DefaultListModel<String>)lModel;
                                Enumeration<String> elements = model.elements();
                                while(elements.hasMoreElements()){
                                    String element = elements.nextElement();
                                    if (element.substring(0, element.length() - 1).endsWith(finalAddress)){
                                        return;
                                    }
                                }
                                model.addElement("(" + finalAddress + ")");
                                connection.registerNetworkListener(new OnReceive(finalAddress));
                                connection.sendMessage(new VerifyConnectionPacketBuilder().setRequest(VerifyConnectionPacketBuilder.REQUEST_TYPE_INIT).setId(NetworkClientPanel.this.nameField.getText()));
                            } catch (IOException ignore) {
                            }
                        }).start();
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    private class LANClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            String selectedItem = NetworkClientPanel.this.lanConnection.getSelectedValue();
            selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
            for(int A = selectedItem.length() - 1; A >= 0; A--){
                if (selectedItem.charAt(A) == '('){
                    selectedItem = selectedItem.substring(A + 1);
                    break;
                }
            }
            NetworkClientPanel.this.ipField.setText(selectedItem);
        }
    }

    public class OnJoin implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                NetworkClientPanel.this.log.setText("\tAttempting to connect");
                ClientConnection connection = new ClientConnection(new Socket(NetworkClientPanel.this.ipField.getText(), Integer.parseInt(NetworkClientPanel.this.portField.getText())));
                Blocks.getInstance().setClient(connection);
                new Thread(connection::connect).start();
                connection.registerNetworkListener(new OnListen());
                NetworkClientPanel.this.log.append("\n\tConnection found\n\tSent request to join");
                Blocks.getInstance().getClient().get().sendMessage(new RequestJoinConnectionPacketBuilder().setTimeout(TimeUnit.SECONDS.toNanos(25)).setIdentifier(NetworkClientPanel.this.nameField.getText()));
            } catch (IOException ioException) {
                NetworkClientPanel.this.log.append("\n\tConnection Failed: " + ioException.getLocalizedMessage());
                ioException.printStackTrace();
            }
        }
    }

    private final JTextField ipField = new JTextField();
    private final JTextField portField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JButton invite = new JButton("Join");
    private final JButton searchLan = new JButton("LAN Search");
    private final JList<String> lanConnection = new JList<>(new DefaultListModel<>());
    private final JTextArea log = new JTextArea();

    public NetworkClientPanel(){
        init();
    }

    private void init(){
        this.searchLan.addActionListener((e) -> {
            this.searchLan.setEnabled(false);
            new Thread(() -> {
                new LanFinder().run();
                this.searchLan.setEnabled(true);
            }).start();
        });
        this.lanConnection.setBackground(null);
        this.lanConnection.addMouseListener(new LANClick());
        this.nameField.setText(System.getenv("COMPUTERNAME"));
        this.portField.setText(ConnectionInfo.DEFAULT_PORT + "");
        this.invite.addActionListener(new OnJoin());
        this.log.setBackground(null);
        this.log.setWrapStyleWord(true);
        this.log.setLineWrap(true);
        this.log.setEditable(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Your name"), c);
        c.gridx = 1;
        c.gridwidth = 2;
        c.weightx = 1.0;
        this.add(this.nameField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        this.add(new JLabel("IP:"), c);
        c.gridx = 1;
        c.weightx = 1.0;
        this.add(this.ipField, c);
        c.gridx = 2;
        c.weightx = 0.0;
        this.add(this.portField, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        this.add(this.invite, c);
        c.gridy = 3;
        this.add(this.searchLan, c);
        c.gridy = 4;
        c.weighty = 1.0;
        this.add(this.log, c);
        c.gridy = 5;
        this.add(this.lanConnection, c);

    }
}
