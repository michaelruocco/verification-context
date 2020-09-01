package uk.co.idv.context.entities.context.create;

import lombok.Builder;
import lombok.Data;
import uk.co.idv.context.entities.activity.Activity;
import uk.co.idv.context.entities.alias.Aliases;
import uk.co.idv.context.entities.channel.Channel;

@Builder
@Data
public class DefaultCreateContextRequest implements CreateContextRequest {

    private final Channel channel;
    private final Aliases aliases;
    private final Activity activity;

}