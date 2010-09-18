/**
 * Copyright 2010 Joseph Panico
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.diffkit.util.tst



import java.sql.Types;

import org.diffkit.db.DKDBConnectionInfo 
import org.diffkit.db.DKDBConnectionSource 
import org.diffkit.util.DKSqlUtil;

import groovy.util.GroovyTestCase;


/**
 * @author jpanico
 */
public class TestSqlUtil extends GroovyTestCase {
	
	public void testReadRowsFromSelect(){
		DKDBConnectionInfo connectionInfo = ['test', DKDBConnectionInfo.Kind.H2,"mem:test", null, null, 'test', 'test']
		println "connectionInfo->$connectionInfo"
		DKDBConnectionSource connectionSource = [connectionInfo]
		def connection = connectionSource.connection
		println "connection->$connection"
		
		assert connection
		def rows = DKSqlUtil.readRows('select * from INFORMATION_SCHEMA.TABLES', connection)
		assert rows
		println "rows->$rows"
		assert rows.find { it['TABLE_NAME'] == 'TABLES'}
		
	}
	
	public void testNameForSqlType(){
		assert DKSqlUtil.getNameForSqlType(Types.VARCHAR) == 'VARCHAR'
		assert DKSqlUtil.getNameForSqlType(Types.BIGINT) == 'BIGINT'
		assert DKSqlUtil.getNameForSqlType(Types.TIMESTAMP) == 'TIMESTAMP'
	}
	
	public void testReadRowsFromResultSet(){
		
		DKDBConnectionInfo connectionInfo = ['test', DKDBConnectionInfo.Kind.H2,"mem:test", null, null, 'test', 'test']
		println "connectionInfo->$connectionInfo"
		DKDBConnectionSource connectionSource = [connectionInfo]
		def connection = connectionSource.connection
		println "connection->$connection"
		
		assert connection
		def meta = connection.metaData
		println "meta->$meta"
		assert meta
		def typeInfo = meta.typeInfo
		assert typeInfo
		
		def typeRows = DKSqlUtil.readRows(typeInfo)
		println "typeRows->$typeRows"
		assert typeRows
		def firstType = typeRows[0]
		assert firstType
		assert firstType.containsKey('TYPE_NAME')
		assert firstType.containsKey('DATA_TYPE')
		assert firstType.containsKey('PRECISION')
		
		assert typeRows.find { it['TYPE_NAME'] == 'BIGINT'}
		assert typeRows.find { it['TYPE_NAME'] == 'CLOB'}
		assert typeRows.find { it['TYPE_NAME'] == 'VARCHAR'}
		
		DKSqlUtil.close(typeInfo)
		DKSqlUtil.close(connection)
	}
	
}