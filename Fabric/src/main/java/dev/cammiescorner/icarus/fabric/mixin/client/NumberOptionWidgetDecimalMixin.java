package dev.cammiescorner.icarus.fabric.mixin.client;

import com.teamresourceful.resourcefulconfig.client.components.options.types.NumberOptionWidget;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NumberOptionWidget.class)
public abstract class NumberOptionWidgetDecimalMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void icarus$forceDotDecimalPresentation(Supplier<?> getter, Function<?, Boolean> setter, Function<String, ?> parser, Pattern filter, CallbackInfo ci) {
        NumberOptionWidget<?> self = (NumberOptionWidget<?>) (Object) this;
        String current = self.getValue();
        if (current.indexOf(',') >= 0) {
            self.setValue(current.replace(',', '.'));
        }
    }
}
