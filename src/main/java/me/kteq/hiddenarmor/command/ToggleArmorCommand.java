package me.kteq.hiddenarmor.command;

import me.kteq.hiddenarmor.HiddenArmor;
import me.kteq.hiddenarmor.armormanager.ArmorManager;
import me.kteq.hiddenarmor.util.CommandUtil;
import me.kteq.hiddenarmor.util.StrUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ToggleArmorCommand {
    HiddenArmor plugin;
    ArmorManager armorManager;

    public ToggleArmorCommand(HiddenArmor pl, ArmorManager am){
        armorManager = am;
        plugin = pl;
        new CommandUtil(plugin,"togglearmor", 0,1, false, plugin.isToggleDefault()){

            @Override
            public void sendUsage(CommandSender sender) {
                if(sender instanceof Player)
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor " + (canUseArg(sender, "other") ? "[player]" : "")));
                else
                    sender.sendMessage(plugin.getPrefix() + StrUtil.color("&2Correct use:&f /togglearmor <player>"));
            }

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){
                Player player;
                if(arguments.length == 1) {
                    if(!canUseArg(sender, "other") && !plugin.isToggleOtherDefault()) return false;
                    String playerName = arguments[0];
                    player = Bukkit.getPlayer(playerName);

                    if(player == null){
                        sender.sendMessage(plugin.getPrefix() + "未找到该玩家.");
                        return true;
                    }
                }else {
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(plugin.getPrefix() + "在控制台使用这个命令时，你必须输入玩家名字: /togglearmor <player>");
                        return true;
                    } else {
                        player = (Player) sender;
                    }
                }

                String visibility;

                if(plugin.hasPlayer(player)){
                    plugin.removeHiddenPlayer(player);
                    visibility = StrUtil.color("&b可见");
                }else {
                    plugin.addHiddenPlayer(player);
                    visibility =  StrUtil.color("&7不可见") ;
                }

                if(!player.equals(sender)) sender.sendMessage(player.getName() + "的装备切换为: " + visibility);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("装备可见度: " + visibility));

                armorManager.updatePlayer(player);

                return true;
            }
        }.setCPermission("toggle").setUsage("/togglearmor").setDescription("切换装备是否可见");
    }
}
