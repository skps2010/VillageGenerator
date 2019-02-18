package game.check;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

import net.minecraft.server.v1_13_R2.BiomeBase;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ChunkCoordIntPair;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.SeededRandom;
import net.minecraft.server.v1_13_R2.StructureBoundingBox;
import net.minecraft.server.v1_13_R2.StructurePiece;
import net.minecraft.server.v1_13_R2.StructureStart;
import net.minecraft.server.v1_13_R2.WorldGenFeatureVillageConfiguration;
import net.minecraft.server.v1_13_R2.WorldGenVillagePieces;
import net.minecraft.server.v1_13_R2.WorldServer;

public class Create_1_13 {
	public static void create(World w, int i, int j, int k, int r, int t) {
		WorldServer ws = ((CraftWorld) w).getHandle();

		Village start = new Village(ws, ws.worldProvider.getChunkGenerator(), new SeededRandom(ws.getSeed()), i >> 4,
				j >> 4, ws.getBiome(new BlockPosition(i, j, 64)), k, t);

		start.a(ws, new Random(), new StructureBoundingBox(i - r, j - r, i + r, j + r),
				new ChunkCoordIntPair(j >> 4, i >> 4));
	}

	public static class Village extends StructureStart {
		private boolean e;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Village(GeneratorAccess generatoraccess, ChunkGenerator<?> chunkgenerator, SeededRandom seededrandom,
				int i, int j, BiomeBase biomebase, int size, int type) {
			super(j, j, biomebase, seededrandom, generatoraccess.getSeed());
			WorldGenFeatureVillageConfiguration worldgenfeaturevillageconfiguration = new WorldGenFeatureVillageConfiguration(
					size, WorldGenVillagePieces.Material.a(type));
			List list = WorldGenVillagePieces.a(seededrandom, size);
			WorldGenVillagePieces.WorldGenVillageStartPiece worldgenvillagepieces_worldgenvillagestartpiece = new WorldGenVillagePieces.WorldGenVillageStartPiece(
					0, seededrandom, (i << 4) + 2, (j << 4) + 2, list, worldgenfeaturevillageconfiguration);

			this.a.add(worldgenvillagepieces_worldgenvillagestartpiece);
			worldgenvillagepieces_worldgenvillagestartpiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a,
					seededrandom);
			List list1 = worldgenvillagepieces_worldgenvillagestartpiece.e;
			List list2 = worldgenvillagepieces_worldgenvillagestartpiece.d;
			while ((!list1.isEmpty()) || (!list2.isEmpty())) {
				if (list1.isEmpty()) {
					int k = seededrandom.nextInt(list2.size());
					StructurePiece structurepiece = (StructurePiece) list2.remove(k);
					structurepiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a, seededrandom);
				} else {
					int k = seededrandom.nextInt(list1.size());
					StructurePiece structurepiece = (StructurePiece) list1.remove(k);
					structurepiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a, seededrandom);
				}
			}
			a(generatoraccess);
			int k = 0;
			Iterator iterator = this.a.iterator();
			while (iterator.hasNext()) {
				StructurePiece structurepiece1 = (StructurePiece) iterator.next();
				if (!(structurepiece1 instanceof WorldGenVillagePieces.WorldGenVillageRoadPiece)) {
					k++;
				}
			}
			this.e = (k > 2);
		}

		public boolean b() {
			return this.e;
		}

		public void a(NBTTagCompound nbttagcompound) {
			super.a(nbttagcompound);
			nbttagcompound.setBoolean("Valid", this.e);
		}

		public void b(NBTTagCompound nbttagcompound) {
			super.b(nbttagcompound);
			this.e = nbttagcompound.getBoolean("Valid");
		}
	}
}
