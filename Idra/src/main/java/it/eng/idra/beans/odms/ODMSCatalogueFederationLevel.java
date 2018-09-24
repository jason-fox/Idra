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
package it.eng.idra.beans.odms;

public enum ODMSCatalogueFederationLevel {
	/*
	 * LEVEL_0: Catalogue added to the federation, no dataset added, no searches enabled
	 * LEVEL_1: Catalogue added to the federation, no dataset added, live search enabled
	 * LEVEL_2: Catalogue added to the federation, dataset added, cache search enabled, synchronization enabled
	 * LEVEL_3: Catalogue added to the federation, dataset added, live/cache search enabled, synchronization enabled
	 * LEVEL_4: Catalogue added to the federation, dataset added, cache search enabled, synchronization not enabled
	 * */
	LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4
	
}
