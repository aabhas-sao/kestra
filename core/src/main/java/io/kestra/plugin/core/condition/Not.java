package io.kestra.plugin.core.condition;

import io.kestra.core.exceptions.InternalException;
import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.annotations.PluginProperty;
import io.kestra.core.models.conditions.Condition;
import io.kestra.core.models.conditions.ConditionContext;
import io.kestra.core.models.conditions.ScheduleCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import static io.kestra.core.utils.Rethrow.throwPredicate;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Schema(
    title = "Condition to exclude others conditions."
)
@Plugin(
    examples = {
        @Example(
            full = true,
            code = {
                "- conditions:",
                "    - type: io.kestra.plugin.core.condition.Not",
                "      conditions:",
                "        -  type: io.kestra.plugin.core.condition.DateTimeBetween",
                "           after: \"2013-09-08T16:19:12Z\"",
            }
        )
    },
    aliases = {"io.kestra.core.models.conditions.types.NotCondition", "io.kestra.plugin.core.condition.NotCondition"}
)
public class Not extends Condition implements ScheduleCondition {
    @NotNull
    @NotEmpty
    @Schema(
        title = "The list of conditions to exclude.",
        description = "If any conditions is true, it will prevent the event's execution."
    )
    @PluginProperty
    private List<Condition> conditions;

    @Override
    public boolean test(ConditionContext conditionContext) throws InternalException {
        return this.conditions
            .stream()
            .noneMatch(throwPredicate(condition -> condition.test(conditionContext)));
    }
}
