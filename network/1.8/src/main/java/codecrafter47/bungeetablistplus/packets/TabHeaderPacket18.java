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

package codecrafter47.bungeetablistplus.packets;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;

public class TabHeaderPacket18 implements ITabHeaderPacket {
    @Override
    public void setTabHeaderFooter(Connection.Unsafe connection, String header, String footer) {
        Preconditions.checkNotNull(header, "header");
        Preconditions.checkNotNull(footer, "footer");
        connection.sendPacket(new PlayerListHeaderFooter(header, footer));
    }
}
