package net.electro.elementalist.client.models.projectiles;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.electro.elementalist.Elementalist;
import net.electro.elementalist.entities.projectiles.IceSpearProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class IceSpearModel extends EntityModel<IceSpearProjectile> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Elementalist.MOD_ID, "ice_spear"), "main");
	private final ModelPart bb_main;

	public IceSpearModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -1.5F, -9.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 10).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-2.0F, -3.0F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(17, 15).addBox(1.0F, -2.0F, -6.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(18, 20).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -3.0F, -3.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(19, 6).addBox(-2.0F, -1.0F, -6.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(13, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(0.0F, -3.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(13, 20).addBox(-2.0F, -1.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 0).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 18).addBox(-0.5F, -1.5F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 17).addBox(0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(13, 6).addBox(-2.0F, -2.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 10).addBox(-2.0F, 0.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 13).addBox(-1.5F, -1.5F, -8.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(IceSpearProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}