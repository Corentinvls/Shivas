package org.shivas.server.core.guilds;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.protocol.client.types.BaseGuildMemberType;
import org.shivas.server.database.models.Guild;
import org.shivas.server.database.models.GuildMember;
import org.shivas.server.database.models.Player;
import org.shivas.server.utils.Converters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 12:21
 */
public class GuildMemberList implements Iterable<GuildMember> {
    private final Guild owner;
    private final EntityRepository<Long, GuildMember> repo;
    private final Map<Integer, GuildMember> members = Maps.newHashMap();

    public GuildMemberList(Guild owner, EntityRepository<Long, GuildMember> repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public int count() {
        return members.size();
    }

    public boolean isEmpty() {
        return count() <= 0;
    }

    public GuildMember get(int playerId) {
        return members.get(playerId);
    }

    public void add(Player player) {
        GuildMember member = new GuildMember(owner, player, GuildRankEnum.TESTING, new GuildMemberRights());
        player.setGuildMember(member);
        repo.persistLater(member);

        add(member);
    }

    public void add(GuildMember member) {
        members.put(member.getPlayer().getId(), member);
    }

    public boolean remove(int playerId) {
        GuildMember member = members.remove(playerId);
        if (member != null) {
            member.getPlayer().setGuildMember(null);
            repo.deleteLater(member);
            return true;
        }
        return false;
    }

    public void remove(Player player) {
        remove(player.getId());
    }

    public Collection<BaseGuildMemberType> toBaseGuildMemberType() {
        return Collections2.transform(members.values(), Converters.GUILDMEMBER_TO_BASEGUILDMEMBERTYPE);
    }

    @Override
    public Iterator<GuildMember> iterator() {
        return members.values().iterator();
    }
}