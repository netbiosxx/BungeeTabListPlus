/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codecrafter47.bungeetablistplus.bridge;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 *
 * @author florian
 */
public class BukkitBridge implements Listener {
    
    private int correntVersion = 1;

    BungeeTabListPlus plugin;

    Map<String, Map<String, String>> serverInformations;
    Map<String, Map<String, String>> playerInformations;

    public BukkitBridge(BungeeTabListPlus plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        serverInformations = new ConcurrentHashMap<>();
        playerInformations = new ConcurrentHashMap<>();
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals(Constants.channel)) {
            if (event.getReceiver() instanceof ProxiedPlayer && event.getSender() instanceof Server) {
                try {
                    ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
                    Server server = (Server) event.getSender();

                    DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

                    String subchannel = in.readUTF();

                    Map<String, String> data = new ConcurrentHashMap<>();
                    int num = in.readInt();
                    while (num-- > 0) {
                        String key = in.readUTF();
                        String value = in.readUTF();
                        data.put(key, value);
                    }

                    if (subchannel.equals(Constants.subchannel_init)) {
                        serverInformations.put(server.getInfo().getName(), data);
                    } else if (subchannel.equals(Constants.subchannel_update)) {
                        if (serverInformations.get(server.getInfo().getName()) == null) {
                            server.sendData(Constants.channel, new byte[]{});
                        } else {
                            for (Entry<String, String> entry : data.entrySet()) {
                                serverInformations.get(server.getInfo().getName()).put(entry.getKey(), entry.getValue());
                            }
                        }
                    } else if (subchannel.equals(Constants.subchannel_initplayer)) {
                        playerInformations.put(player.getName(), data);
                        data.put("server", server.getInfo().getName());
                    }else if(subchannel.equals(Constants.subchannel_updateplayer)){
                        if(playerInformations.get(player.getName()) == null){
                            server.sendData(Constants.channel, new byte[]{});
                        } else {
                            for(Entry<String,String> entry: data.entrySet()){
                                playerInformations.get(player.getName()).put(entry.getKey(), entry.getValue());
                            }
                        }
                    }else{
                        plugin.getLogger().log(Level.SEVERE, "BukkitBridge on server " + server.getInfo().getName() + " send an unknown packet! Is everything up-to-date?");
                    }

                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Exception while parsing data from Bukkit", ex);
                }
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onServerChange(ServerConnectedEvent event){
        ProxiedPlayer player = event.getPlayer();
        if(!playerInformations.get(player.getName()).get("server").equals(player.getServer().getInfo().getName())){
            playerInformations.remove(player.getName());
        }
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        playerInformations.remove(player.getName());
    }
    
    @EventHandler
    public void onPlayerKick(ServerKickEvent event){
        ProxiedPlayer player = event.getPlayer();
        playerInformations.remove(player.getName());
    }
    
    public String getServerInformation(ServerInfo server, String key){
        Map<String, String> map = serverInformations.get(server.getName());
        if(map == null)return null;
        return map.get(key);
    }
    
    public String getPlayerInformation(ProxiedPlayer player, String key){
        Map<String, String> map = playerInformations.get(player.getName());
        if(map == null)return null;
        return map.get(key);
    }
    
    public boolean isServerInformationAvailable(ServerInfo server){
        return serverInformations.get(server.getName()) != null;
    }
    
    public boolean isPlayerInformationAvailable(ProxiedPlayer player){
        return playerInformations.get(player.getName()) != null;
    }
    
    public boolean isUpToDate(String server){
        try{
            return Integer.valueOf(serverInformations.get(server).get("bridgeVersion")) >= this.correntVersion;
        }catch(Throwable ex){
            // passiert halt
        }
        return true;
    }
}
