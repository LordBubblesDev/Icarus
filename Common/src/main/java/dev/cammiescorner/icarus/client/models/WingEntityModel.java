// Made with Model Converter by Globox_Z
// Generate all required imports
package dev.cammiescorner.icarus.client.models;

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
	public void setupAnim(HumanoidRenderState renderState) {
		super.setupAnim(renderState);
		this.state = State.IDLE;
		float a = 0.125F;
		float b = 0.1F;
		float xRot = renderState.elytraRotX;
		float zRot = renderState.elytraRotZ;
		float yOffset = renderState.isCrouching ? 0.0F : -1.0F;
		float yRot = renderState.elytraRotY;

		if(renderState.isFallFlying) {
			this.state = State.FLYING;
			if(renderState.speedValue > 1.0F) {
				a = 0.4F;
				b = 1.0F;
			}
		}
		else if(renderState.isCrouching) {
			this.state = State.CROUCHING;
			xRot = 0.7F;
			yOffset = 0.0F;
			yRot = 0.09F;
		}

		xRot += Mth.sin(renderState.ageInTicks * a) * b;
		this.leftWing.x = 7.0F;
		this.leftWing.y = yOffset;

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
