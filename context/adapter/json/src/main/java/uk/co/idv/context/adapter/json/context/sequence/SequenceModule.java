package uk.co.idv.context.adapter.json.context.sequence;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import uk.co.idv.context.adapter.json.context.method.MethodModule;
import uk.co.idv.context.entities.context.sequence.Sequence;
import uk.co.idv.context.entities.context.sequence.Sequences;
import uk.co.idv.method.adapter.json.method.MethodMapping;
import uk.co.idv.method.adapter.json.method.MethodMappings;

import java.util.Collections;

public class SequenceModule extends SimpleModule {

    private final transient MethodMappings mappings;

    public SequenceModule(MethodMapping... mappings) {
        this(new MethodMappings(mappings));
    }

    public SequenceModule(MethodMappings mappings) {
        super("sequence-module", Version.unknownVersion());
        this.mappings = mappings;

        setMixInAnnotation(Sequence.class, SequenceMixin.class);
        setMixInAnnotation(Sequences.class, SequencesMixin.class);

        addDeserializer(Sequence.class, new SequenceDeserializer());
        addDeserializer(Sequences.class, new SequencesDeserializer());
    }

    @Override
    public Iterable<? extends Module> getDependencies() {
        return Collections.singleton(new MethodModule(mappings));
    }

}
