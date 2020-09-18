package org.block.project.panel.network;

import org.block.Blocks;
import org.block.network.ConnectionInfo;
import org.block.network.client.ClientConnection;
import org.block.network.common.event.NetEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.request.RequestJoinConnectionPacketBuilder;
import org.block.network.common.packets.request.RequestJoinEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

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

    public class OnJoin implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                NetworkClientPanel.this.log.setText("\tAttempting to connect");
                ClientConnection connection = new ClientConnection(new Socket(NetworkClientPanel.this.ipField.getText(), Integer.parseInt(NetworkClientPanel.this.portField.getText())));
                Blocks.getInstance().setClient(connection);
                connection.registerNetworkListener(new OnListen());
                NetworkClientPanel.this.log.append("\n\tConnection found\n\tSent request to join");
                Blocks.getInstance().getClient().get().sendMessage(new RequestJoinConnectionPacketBuilder().setIdentifier(NetworkClientPanel.this.nameField.getText()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private final JTextField ipField = new JTextField();
    private final JTextField portField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JButton invite = new JButton("Join");
    private final JTextArea log = new JTextArea();

    public NetworkClientPanel(){
        init();
    }

    private void init(){
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
        c.weighty = 1.0;
        this.add(this.log, c);

    }

    public static void main(String[] args){
        Blocks.setInstance(new Blocks());
        NetworkClientPanel panel = new NetworkClientPanel();
        JFrame frame = new JFrame("Network test");
        frame.setSize(300, 500);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
