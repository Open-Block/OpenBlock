package org.block.project.legacypanel.network;

import com.dosse.upnp.UPnP;
import org.block.Blocks;
import org.block.network.ConnectionInfo;
import org.block.network.common.event.NetEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.project.SendProjectPacketBuilder;
import org.block.network.common.packets.request.RequestJoinConnectionPacketBuilder;
import org.block.network.common.packets.request.RequestJoinEvent;
import org.block.network.common.packets.request.RequestPacketValue;
import org.block.network.common.packets.verify.VerifyConnectionEvent;
import org.block.network.server.ServerConnection;
import org.block.project.module.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;

public class NetworkServerPanel extends JPanel {

    public class OnListen implements NetworkListener {

        @NetEvent
        public void onReceive(VerifyConnectionEvent event){
            //event.getConnection().sendMessage(new VerifyConnectionPacketBuilder().setId(NetworkServerPanel.this.nameField.getName()).setRequest(VerifyConnectionPacketBuilder.REQUEST_TYPE_INIT));
        }

        @NetEvent
        public void onReceive(RequestJoinEvent event){
            NetworkServerPanel.this.invites.updateValues();
            NetworkServerPanel.this.invites.updateGraphics();
        }

    }

    public class InvitePanel extends JPanel {

        public class InviteAccount extends JPanel {

            private class AcceptButton implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Project.Loaded project = Blocks.getInstance().getLoadedProject().get();
                    InviteAccount.this.setVisible(false);
                    InvitePanel.this.values.remove(InviteAccount.this);
                    InviteAccount.this.value.getTargetConnection().sendMessage(new SendProjectPacketBuilder().setProject(project));
                }
            }

            private class RejectButton implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    InviteAccount.this.setVisible(false);
                    InvitePanel.this.values.remove(InviteAccount.this);
                    InviteAccount.this.value.getTargetConnection().sendMessage(new RequestJoinConnectionPacketBuilder().setIdentifier(NetworkServerPanel.this.nameField.getText()).setAccepting(false));
                }
            }

            private final RequestPacketValue value;
            private final JProgressBar bar = new JProgressBar();
            private final JButton accept = new JButton("Accept");
            private final JButton reject = new JButton("Reject");

            public InviteAccount(RequestPacketValue value){
                this.value = value;
                init();
            }

            private void init(){
                this.accept.addActionListener(new AcceptButton());
                this.reject.addActionListener(new RejectButton());
                this.bar.setString(value.getUsername());
                this.bar.setStringPainted(true);
                this.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.gridy = 0;
                c.gridx = 0;
                c.weighty = 1.0;
                c.fill = GridBagConstraints.BOTH;
                this.add(this.accept, c);
                c.gridx = 1;
                c.weightx = 1.0;
                this.add(this.bar, c);
                c.gridx = 2;
                c.weightx = 0.0;
                this.add(this.reject, c);
            }

            private void updateTime(){
                long fullDiff = ChronoUnit.MILLIS.between(this.value.getTime(), this.value.getTimeoutTime());
                long compare = ChronoUnit.MILLIS.between(this.value.getTime(), LocalTime.now());
                this.bar.setMaximum((int)fullDiff);
                this.bar.setValue((int)compare);
            }

            @Override
            public boolean equals(Object obj) {
                if(!(obj instanceof InviteAccount)){
                    return false;
                }
                return this.value.equals(((InviteAccount)obj).value);
            }
        }

        private Set<InviteAccount> values = new HashSet<>();

        public void register(RequestPacketValue value){
            this.values.add(new InviteAccount(value));
            updateGraphics();
        }

        public void updateValues(){
            if(!Blocks.getInstance().getServer().isPresent()) {
                return;
            }
            ServerConnection connection = Blocks.getInstance().getServer().get();
            connection.getPacketValue(RequestPacketValue.class).forEach(v -> {
                InviteAccount inviteAccount = new InviteAccount(v);
                this.values.add(inviteAccount);
                new Thread(() -> {
                    while (this.isShowing()){
                        inviteAccount.updateTime();
                        if (inviteAccount.value.hasTimedOut() && inviteAccount.isVisible()){
                            inviteAccount.setVisible(false);
                            inviteAccount.value.getTargetConnection().sendMessage(new RequestJoinConnectionPacketBuilder().setIdentifier(NetworkServerPanel.this.nameField.getText()).setAccepting(false));
                        }
                    }
                }).start();
            });
            Set<InviteAccount> remove = new HashSet<>();
            for (InviteAccount account : this.values) {
                if (account.value.hasTimedOut()) {
                    remove.add(account);
                }
            }
            this.values.removeAll(remove);
        }

        public void updateGraphics(){
            List<InviteAccount> list = new ArrayList<>(this.values);
            list.sort(Comparator.comparingLong(r -> r.value.getTimeoutTime().toNanoOfDay()));
            this.removeAll();
            this.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.BOTH;
            for(InviteAccount account : list){
                this.add(account, c);
                c.gridy++;
            }
            this.repaint();
            this.revalidate();
        }
    }

    public class OnStartServer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(NetworkServerPanel.this.startServer.getText().equalsIgnoreCase("Start Server")) {
                NetworkServerPanel.this.startServer.setEnabled(false);
                new Thread(() -> {
                    int port = Integer.parseInt(NetworkServerPanel.this.port.getText());
                    UPnP.openPortTCP(port);
                    try {
                        ServerConnection server = new ServerConnection(new ServerSocket(port));
                        Blocks.getInstance().setServer(server);
                        server.registerNetworkListener(new OnListen());
                        NetworkServerPanel.this.startServer.setText("Stop Server");
                        NetworkServerPanel.this.startServer.setEnabled(true);
                        server.acceptConnections();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }).start();
            }else{
                int port = Integer.parseInt(NetworkServerPanel.this.port.getText());
                UPnP.openPortTCP(port);
                new Thread(() -> {
                    ServerConnection server = Blocks.getInstance().getServer().get();
                    server.registerNetworkListener(new OnListen());
                    server.shutdownConnections();
                    try {
                        server.getTargetSocket().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    Blocks.getInstance().setServer(null);
                    NetworkServerPanel.this.startServer.setText("Start Server");
                    NetworkServerPanel.this.startServer.setEnabled(true);
                }).start();
            }
        }
    }

    private final JTextField externalIP4 = new JTextField();
    private final JTextField externalIP6 = new JTextField();
    private final Map<JLabel, JTextField> localNetworks = new HashMap<>();
    private final JTextField port = new JTextField();
    private final JTextField nameField = new JTextField();
    private final InvitePanel invites = new InvitePanel();
    private final JButton startServer = new JButton("Start Server");

    public NetworkServerPanel(){
        init();
    }

    private void init(){
        this.nameField.setText(System.getenv("COMPUTERNAME"));
        this.externalIP4.setEditable(false);
        this.externalIP4.setBackground(null);
        this.externalIP6.setEditable(false);
        this.externalIP6.setBackground(null);
        this.port.setText(ConnectionInfo.DEFAULT_PORT + "");
        this.startServer.addActionListener(new OnStartServer());
        new Thread(() -> {
            try {
                URL url = new URL(ConnectionInfo.IP_4_CHECK_URL);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                this.externalIP4.setText(br.readLine());
            } catch (IOException e) {
                this.externalIP4.setText(e.getLocalizedMessage());
            }
            try {
                URL url = new URL(ConnectionInfo.IP_6_CHECK_URL);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                this.externalIP6.setText(br.readLine());
                if(this.externalIP6.getText().split("\\.").length != 6){
                    this.externalIP6.setText("No IP6 found");
                    this.externalIP6.setForeground(Color.RED);

                }
            } catch (IOException e) {
                this.externalIP6.setText(e.getLocalizedMessage());
                this.externalIP6.setForeground(Color.RED);
            }
        }).start();


        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()){
                NetworkInterface net = networks.nextElement();
                if (net.isVirtual()){
                    continue;
                }
                if (!net.isUp()){
                    continue;
                }
                if(net.getInterfaceAddresses().isEmpty()){
                    continue;
                }
                if(net.isLoopback()){
                    continue;
                }

                String nameID = net.getName();
                String dName = net.getDisplayName();
                String display = nameID + " (LAN) (" + dName + "):";
                if(dName.equals("LogMeIn Hamachi Virtual Ethernet Adapter")){
                    display = "Hamachi (Internet):";
                    dName = display;
                    nameID = display;
                }
                if(dName.equals("VirtualBox Host-Only Ethernet Adapter")){
                    display = "VirtualBox (Local):";
                    dName = display;
                    nameID = display;
                }
                if(nameID.startsWith("wlan") || nameID.startsWith("eth")){
                    if(nameID.startsWith("wlan")){
                        display = "Wi-Fi";
                    }else if(nameID.startsWith("eth")){
                        display = "Ethernet";
                    }
                    if(dName.startsWith("Intel(R) Dual Band Wireless-AC")) {
                        display = display + " (Primary LAN)";
                    }else if(dName.toLowerCase().contains("ndis")){
                        display = display + " (LAN) (NDIS/RNDIS - Probably USB)";
                    }else {
                        display = display + " (LAN) (" + dName + "):";
                    }
                }
                JLabel name = new JLabel(display);
                JTextField add = new JTextField(net.getInterfaceAddresses().get(0).getAddress().getHostAddress());
                name.setSize(add.getPreferredSize());
                add.setBackground(null);
                add.setEditable(false);
                this.localNetworks.put(name, add);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        JPanel connectionInfoPanel = new JPanel();
        connectionInfoPanel.setLayout(new GridLayout(this.localNetworks.size() + 2, 2));
        JLabel ip4Label = new JLabel("IP4 (Internet):");
        JLabel ip6Label = new JLabel("IP6 (Internet):");
        connectionInfoPanel.add(ip4Label);
        connectionInfoPanel.add(this.externalIP4);
        connectionInfoPanel.add(ip6Label);
        connectionInfoPanel.add(this.externalIP6);

        for(Map.Entry<JLabel, JTextField> e : this.localNetworks.entrySet()){
            connectionInfoPanel.add(e.getKey());
            connectionInfoPanel.add(e.getValue());
        }

        connectionInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createDashedBorder(Color.BLACK, 8, 2), "Connection info"));

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(connectionInfoPanel, c);
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0;
        this.add(new JLabel("Port"), c);
        c.weightx = 1.0;
        c.gridx = 1;
        this.add(this.port, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        this.add(this.startServer, c);
        c.gridy = 3;
        c.weighty = 1.0;

        this.add(new JScrollPane(this.invites), c);
    }
}
