/* Generated By:JJTree: Do not edit this line. ODropClusterStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.sql.executor.OInternalResultSet;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import java.util.Map;

public class ODropClusterStatement extends ODDLStatement {
  protected OIdentifier name;
  protected OInteger id;
  protected boolean ifExists = false;

  public ODropClusterStatement(int id) {
    super(id);
  }

  public ODropClusterStatement(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public OResultSet executeDDL(OCommandContext ctx) {
    ODatabaseInternal database = (ODatabaseInternal) ctx.getDatabase();
    // CHECK IF ANY CLASS IS USING IT
    final int clusterId;
    if (id != null) {
      clusterId = id.getValue().intValue();
    } else {
      clusterId = database.getStorage().getClusterIdByName(name.getStringValue());
      if (clusterId < 0) {
        if (ifExists) {
          return new OInternalResultSet();
        } else {
          throw new OCommandExecutionException("Cluster not found: " + name);
        }
      }
    }
    for (OClass iClass : database.getMetadata().getSchema().getClasses()) {
      for (int i : iClass.getClusterIds()) {
        if (i == clusterId) {
          // IN USE
          throw new OCommandExecutionException(
              "Cannot drop cluster "
                  + clusterId
                  + " because it's used by class "
                  + iClass.getName());
        }
      }
    }

    // REMOVE CACHE OF COMMAND RESULTS IF ACTIVE
    String clusterName = database.getClusterNameById(clusterId);
    if (clusterName == null) {
      if (ifExists) {
        return new OInternalResultSet();
      } else {
        throw new OCommandExecutionException("Cluster not found: " + clusterId);
      }
    }

    database.dropCluster(clusterId);

    OInternalResultSet rs = new OInternalResultSet();
    OResultInternal result = new OResultInternal();
    result.setProperty("operation", "drop cluster");
    result.setProperty("clusterName", name == null ? null : name.getStringValue());
    result.setProperty("clusterId", id == null ? null : id.getValue());
    rs.add(result);
    return rs;
  }

  @Override
  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append("DROP CLUSTER ");
    if (name != null) {
      name.toString(params, builder);
    } else {
      id.toString(params, builder);
    }
    if (ifExists) {
      builder.append(" IF EXISTS");
    }
  }

  @Override
  public void toGenericStatement(StringBuilder builder) {
    builder.append("DROP CLUSTER ");
    if (name != null) {
      name.toGenericStatement(builder);
    } else {
      id.toGenericStatement(builder);
    }
    if (ifExists) {
      builder.append(" IF EXISTS");
    }
  }

  @Override
  public ODropClusterStatement copy() {
    ODropClusterStatement result = new ODropClusterStatement(-1);
    result.name = name == null ? null : name.copy();
    result.id = id == null ? null : id.copy();
    result.ifExists = this.ifExists;
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ODropClusterStatement that = (ODropClusterStatement) o;

    if (ifExists != that.ifExists) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return id != null ? id.equals(that.id) : that.id == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    result = 31 * result + (ifExists ? 1 : 0);
    return result;
  }
}
/* JavaCC - OriginalChecksum=239ffe92e79e1d5c82976ed9814583ec (do not edit this line) */
