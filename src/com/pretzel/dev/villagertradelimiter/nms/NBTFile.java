package com.pretzel.dev.villagertradelimiter.nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pretzel.dev.villagertradelimiter.nms.utils.nmsmappings.ObjectCreator;

/**
 * {@link NBTCompound} implementation backed by a {@link File}
 * 
 * @author tr7zw
 *
 */
public class NBTFile extends NBTCompound {

	private final File file;
	private Object nbt;

	/**
	 * Creates a NBTFile that uses @param file to store it's data. If this file
	 * exists, the data will be loaded.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public NBTFile(File file) throws IOException {
		super(null, null);
		if (file == null) {
			throw new NullPointerException("File can't be null!");
		}
		this.file = file;
		if (file.exists()) {
			FileInputStream inputsteam = new FileInputStream(file);
			nbt = NBTReflectionUtil.readNBT(inputsteam);
		} else {
			nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
			save();
		}
	}

	/**
	 * Saves the data to the file
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		try {
			getWriteLock().lock();
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				if (!file.createNewFile())
					throw new IOException("Unable to create file at " + file.getAbsolutePath());
			}
			FileOutputStream outStream = new FileOutputStream(file);
			NBTReflectionUtil.writeNBT(nbt, outStream);
		} finally {
			getWriteLock().unlock();
		}
	}

	/**
	 * @return The File used to store the data
	 */
	public File getFile() {
		return file;
	}

	@Override
	public Object getCompound() {
		return nbt;
	}

	@Override
	protected void setCompound(Object compound) {
		nbt = compound;
	}

}
