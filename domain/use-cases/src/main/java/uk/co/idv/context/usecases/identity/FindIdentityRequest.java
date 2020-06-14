package uk.co.idv.context.usecases.identity;

import com.neovisionaries.i18n.CountryCode;
import uk.co.idv.context.entities.alias.Aliases;
import uk.co.idv.context.entities.channel.Channel;

public interface FindIdentityRequest {

    String getChannelId();

    Channel getChannel();

    CountryCode getCountry();

    Aliases getAliases();

}
