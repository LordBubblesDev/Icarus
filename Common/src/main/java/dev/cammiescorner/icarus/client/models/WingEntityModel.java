// Made with Model Converter by Globox_Z
// Generate all required imports
package dev.cammiescorner.icarus.client.models;

import dev.cammiescorner.icarus.client.IcarusSlowFallRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class WingEntityModel extends EntityModel<HumanoidRenderState> {
	public final ModelPart rightWing;
	public final ModelPart leftWing;
	public State state = State.IDLE;

	public WingEntityModel(ModelPart root) {
		super(root);
		this.rightWing = root.getChild("rightWing");
		this.leftWing = root.getChild("leftWing");
	}

	public static MeshDefinition getModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		modelPartData.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));
		modelPartData.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

		return modelData;
	}

	@Override
	public void setupAnim(HumanoidRenderState renderState) {
		super.setupAnim(renderState);
		boolean isIcarusSlowFalling = renderState instanceof IcarusSlowFallRenderState slowState && slowState.icarus$isSlowFallingWithWings();

		if (renderState.isFallFlying) {
			this.state = State.FLYING;
			float a = 0.125F;
			float b = 0.1F;
			if (renderState.speedValue > 1.0F) {
				a = 0.4F;
				b = 1.0F;
			}
			// Match ElytraModel: Y offset when crouched; rotations from HumanoidRenderState (flight targets from ElytraAnimationState).
			float wingY = renderState.isCrouching ? 3.0F : 0.0F;
			float xRot = renderState.elytraRotX + Mth.sin(renderState.ageInTicks * a) * b;
			applySymmetricWings(wingY, xRot, renderState.elytraRotZ, renderState.elytraRotY);
			return;
		}

		// Ground / slow-fall: same layout as vanilla ElytraModel — driven by elytraRot* + crouch Y.
		this.state = renderState.isCrouching ? State.CROUCHING : State.IDLE;
		float wingY = renderState.isCrouching ? 3.0F : 0.0F;
		float xRot = renderState.elytraRotX;
		if (isIcarusSlowFalling) {
			xRot += Mth.sin(renderState.ageInTicks * 0.2F) * 0.5F;
		}
		applySymmetricWings(wingY, xRot, renderState.elytraRotZ, renderState.elytraRotY);
	}

	private void applySymmetricWings(float wingY, float xRot, float zRot, float yRot) {
		this.leftWing.x = 7.0F;
		this.leftWing.y = wingY;
		this.leftWing.xRot = xRot;
		this.leftWing.zRot = zRot;
		this.leftWing.yRot = yRot;
		this.rightWing.x = -this.leftWing.x;
		this.rightWing.yRot = -this.leftWing.yRot;
		this.rightWing.y = this.leftWing.y;
		this.rightWing.xRot = this.leftWing.xRot;
		this.rightWing.zRot = -this.leftWing.zRot;
	}

	public enum State {
		IDLE, CROUCHING, FLYING
	}
}
