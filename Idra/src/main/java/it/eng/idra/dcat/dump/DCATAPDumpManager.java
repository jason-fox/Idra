/*******************************************************************************
 * Idra - Open Data Federation Platform
 *  Copyright (C) 2018 Engineering Ingegneria Informatica S.p.A.
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *  
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.eng.idra.dcat.dump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import it.eng.idra.beans.IdraProperty;
import it.eng.idra.beans.dcat.DCATAPFormat;
import it.eng.idra.beans.dcat.DCATAPProfile;
import it.eng.idra.beans.dcat.DCATAPWriteType;
import it.eng.idra.beans.exception.DatasetNotFoundException;
import it.eng.idra.beans.search.SearchResult;
import it.eng.idra.cache.MetadataCacheManager;
import it.eng.idra.utils.PropertyManager;

public class DCATAPDumpManager {

	public static Logger logger = LogManager.getLogger(DCATAPDumpManager.class);

	private static final Boolean dumpOnStart = Boolean.parseBoolean(PropertyManager.getProperty(IdraProperty.DUMP_ON_START));
	private static final Long dumpPeriod = Long.parseLong(PropertyManager.getProperty(IdraProperty.DUMP_PERIOD)) * 1000;
	private static DCATAPFormat dumpFormat = DCATAPFormat
			.fromString(PropertyManager.getProperty(IdraProperty.DUMP_FORMAT));
	private static DCATAPProfile dumpProfile = DCATAPProfile
			.fromString(PropertyManager.getProperty(IdraProperty.DUMP_PROFILE));
	private static  Boolean dumpZip = Boolean.parseBoolean(PropertyManager.getProperty(IdraProperty.DUMP_ZIP_FILE));
	private static final String globalDumpFilePath = PropertyManager.getProperty(IdraProperty.DUMP_FILE_PATH);
	public static final String globalDumpFileName = PropertyManager.getProperty(IdraProperty.DUMP_FILE_NAME);

	
	private DCATAPDumpManager() {
	}

	public static byte[] getDatasetDumpFromFile(String nodeID, Boolean forceDump, Boolean returnZip)
			throws IOException, NumberFormatException, DatasetNotFoundException, SolrServerException {
		/*
		 * Non necessario unzippare perchè ci teniamo entrambe le versioni del dump
		 * 
		 */
		// ZipFile zipFile = new ZipFile(globalDumpFilePath + globalDumpFileName + ".zip");
		// InputStream in = null;
		// ByteArrayOutputStream out = null;
		// try {
		// in = zipFile.getInputStream(zipFile.getEntry(globalDumpFileName));
		// out = new ByteArrayOutputStream();
		// IOUtils.copy(in, out);
		// return out.toByteArray();
		// } finally {
		// zipFile.close();
		// IOUtils.closeQuietly(in);
		// IOUtils.closeQuietly(out);
		// }

		try {
			if (forceDump) {
				if (StringUtils.isBlank(nodeID))
					return DCATAPSerializer.searchResultToDCATAP(MetadataCacheManager.searchAllDatasets(), dumpFormat,
							dumpProfile, DCATAPWriteType.FILE).getBytes();
				else
					return DCATAPSerializer.searchResultToDCATAPByNode(nodeID,
							MetadataCacheManager.searchAllDatasetsByODMSNode(Integer.parseInt(nodeID)), dumpFormat,
							dumpProfile, DCATAPWriteType.FILE).getBytes();
			} else
				return Files.readAllBytes(Paths.get(
						globalDumpFilePath + globalDumpFileName + (StringUtils.isBlank(nodeID) ? "" : new String("_node_" + nodeID))
								+ (returnZip ? ".zip" : "")));

		} catch (NoSuchFileException e) {
			logger.info("No dump file found" + (StringUtils.isNotBlank(nodeID) ? ("for nodeID: " + nodeID)
					: "" + " Starting to create a new one"));

			// TODO Provide dumpReady flag of the Node for availability polling

			// Create the dump file for the relative node

			if (StringUtils.isBlank(nodeID))
				return DCATAPSerializer.searchResultToDCATAP(MetadataCacheManager.searchAllDatasets(), dumpFormat,
						dumpProfile, DCATAPWriteType.FILE).getBytes();
			else
				return DCATAPSerializer.searchResultToDCATAPByNode(nodeID,
						MetadataCacheManager.searchAllDatasetsByODMSNode(Integer.parseInt(nodeID)), dumpFormat,
						dumpProfile, DCATAPWriteType.FILE).getBytes();

		}

	}
}
