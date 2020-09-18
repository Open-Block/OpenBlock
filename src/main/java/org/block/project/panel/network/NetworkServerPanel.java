package org.block.project.panel.network;

import org.block.Blocks;
import org.block.network.ConnectionInfo;
import org.block.network.common.event.NetEvent;
import org.block.network.common.event.NetworkListener;
import org.block.network.common.packets.request.RequestJoinEvent;
import org.block.network.common.packets.request.RequestPacketValue;
import org.block.network.server.ServerConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkServerPanel extends JPanel {

    public class OnListen implements NetworkListener {

        @NetEvent
        public void onReceive(RequestJoinEvent event){
            NetworkServerPanel.this.invites.updateValues();
            NetworkServerPanel.this.invites.updateGraphics();
        }

    }

    public class InvitePanel extends JPanel {

        private Set<RequestPacketValue> values = new HashSet<>();

        public void register(RequestPacketValue value){
            this.values.add(value);
            updateGraphics();
        }

        public void updateValues(){
            if(!Blocks.getInstance().getServer().isPresent()) {
                return;
            }
            ServerConnection connection = Blocks.getInstance().getServer().get();
            this.values.addAll(connection.getPacketValue(RequestPacketValue.class));
            this.repaint();
            this.revalidate();
        }

        public void updateGraphics(){
            LocalTime time = LocalTime.now();
            this.values.removeAll(this.values.stream().filter(r -> r.getTimeoutTime().isBefore(time)).collect(Collectors.toList()));
            List<RequestPacketValue> list = new ArrayList<>(this.values);
            list.sort(Comparator.comparingLong(r -> r.getTimeoutTime().toNanoOfDay()));
            this.removeAll();
            this.setLayout(new GridLayout(1, list.size()));
            for(RequestPacketValue value : list){
                JProgressBar bar = new JProgressBar();
                bar.setMaximum(value.getTimeout().orElse(LocalTime.MAX.toNanoOfDay()).intValue());
                bar.setValue((int)(time.toNanoOfDay() - value.getTime().toNanoOfDay()));
                bar.setString(value.getUsername());
                bar.setStringPainted(true);
                this.add(bar);
            }
            this.repaint();
            this.revalidate();
        }
    }

    public class OnStartServer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    ServerConnection server = new ServerConnection(new ServerSocket(Integer.parseInt(NetworkServerPanel.this.port.getText())));
                    Blocks.getInstance().setServer(server);
                    server.registerNetworkListener(new OnListen());
                    server.acceptConnections();
                    NetworkServerPanel.this.startServer.setEnabled(false);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }).start();
        }
    }

    private final JLabel externalIP4 = new JLabel();
    private final JLabel externalIP6 = new JLabel();
    private final JTextField port = new JTextField();
    private final InvitePanel invites = new InvitePanel();
    private final JButton startServer = new JButton("Start Server");

    public NetworkServerPanel(){
        init();
    }

    private void init(){
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
        JPanel connectionInfoPanel = new JPanel();
        connectionInfoPanel.setLayout(new GridLayout(2, 2));
        connectionInfoPanel.add(new JLabel("IP4:"));
        connectionInfoPanel.add(this.externalIP4);
        connectionInfoPanel.add(new JLabel("IP6:"));
        connectionInfoPanel.add(this.externalIP6);
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

    public static void main(String[] args){
        Blocks.setInstance(new Blocks());
        NetworkServerPanel panel = new NetworkServerPanel();
        JFrame frame = new JFrame("Network test");
        frame.setSize(300, 500);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
