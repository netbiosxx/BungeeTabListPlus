/*
 * BungeeTabListPlus - a BungeeCord plugin to customize the tablist
 *
 * Copyright (C) 2014 - 2015 Florian Stober
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package codecrafter47.bungeetablistplus.playersorting.rules;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import codecrafter47.bungeetablistplus.api.bungee.IPlayer;
import codecrafter47.bungeetablistplus.api.bungee.tablist.TabListContext;
import codecrafter47.bungeetablistplus.bridge.BukkitBridge;
import codecrafter47.bungeetablistplus.context.Context;
import codecrafter47.bungeetablistplus.player.Player;
import codecrafter47.bungeetablistplus.playersorting.SortingRule;
import de.codecrafter47.data.minecraft.api.MinecraftData;

import java.text.Collator;
import java.util.Optional;

public class WorldByName implements SortingRule {
    @Override
    public int compare(TabListContext context, IPlayer player1, IPlayer player2) {
        BukkitBridge bridge = BungeeTabListPlus.getInstance().getBridge();
        Optional<String> faction1 = ((Player) player1).getOpt(MinecraftData.World);
        Optional<String> faction2 = ((Player) player2).getOpt(MinecraftData.World);
        if (faction1.isPresent() || faction2.isPresent()) {
            return Collator.getInstance().compare(faction1.orElse(""), faction2.orElse(""));
        }
        return 0;
    }

    @Override
    public int compare(Context context, IPlayer player1, IPlayer player2) {
        BukkitBridge bridge = BungeeTabListPlus.getInstance().getBridge();
        Optional<String> faction1 = ((Player) player1).getOpt(MinecraftData.World);
        Optional<String> faction2 = ((Player) player2).getOpt(MinecraftData.World);
        if (faction1.isPresent() || faction2.isPresent()) {
            return Collator.getInstance().compare(faction1.orElse(""), faction2.orElse(""));
        }
        return 0;
    }
}
