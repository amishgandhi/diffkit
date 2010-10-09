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
package org.diffkit.db;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.NotImplementedException;
import org.diffkit.common.DKValidate;

/**
 * @author jpanico
 */
public class DKDBConnectionInfo {
   public enum Kind {
      H2("org.h2.Driver"), MYSQL(""), ORACLE(""), DB2("com.ibm.db2.jcc.DB2Driver"), SYBASE(
         "");

      public final String _driverName;

      private Kind(String driverName_) {
         _driverName = driverName_;
         DKValidate.notNull(_driverName);
      }
   }

   private final String _name;
   private final Kind _kind;
   private final String _database;
   private final String _host;
   private final Long _port;
   private final String _username;
   private final String _password;

   public DKDBConnectionInfo(String name_, Kind kind_, String database_, String host_,
                             Long port_, String username_, String password_) {
      _name = name_;
      _kind = kind_;
      _database = database_;
      _host = host_;
      _port = port_;
      _username = username_;
      _password = password_;

      DKValidate.notNull(_name, _kind, _database, _username, _password);
   }

   public String getJDBCUrl() {
      switch (_kind) {
      case H2:
         return this.getH2Url();
      case DB2:
         return this.getDB2Url();

      default:
         throw new NotImplementedException();
      }
   }

   public String getDriverName() {
      return _kind._driverName;
   }

   private String getH2Url() {
      return "jdbc:h2:" + _database;
   }

   // jdbc:db2://<host>[:<port>]/<database_name>
   private String getDB2Url() {
      return String.format("jdbc:db2://%s:%s/%s", _host, _port, _database);
   }

   public String getUsername() {
      return _username;
   }

   public String getPassword() {
      return _password;
   }

   public String toString() {
      return String.format("%s[%s]", ClassUtils.getShortClassName(this.getClass()),
         this.getJDBCUrl());
   }
}