/* Generated By:JJTree: Do not edit this line. OFindReferencesStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OBasicCommandContext;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.sql.executor.OFindReferencesExecutionPlanner;
import com.orientechnologies.orient.core.sql.executor.OInternalExecutionPlan;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OFindReferencesStatement extends OStatement {
  protected ORid rid;
  protected OStatement subQuery;

  // class or cluster
  protected List<SimpleNode> targets;

  public OFindReferencesStatement(int id) {
    super(id);
  }

  public OFindReferencesStatement(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public boolean isIdempotent() {
    return true;
  }

  @Override
  public OResultSet execute(
      ODatabase db, Object[] args, OCommandContext parentCtx, boolean usePlanCache) {
    OBasicCommandContext ctx = new OBasicCommandContext();
    if (parentCtx != null) {
      ctx.setParentWithoutOverridingChild(parentCtx);
    }
    ctx.setDatabase(db);
    Map<Object, Object> params = new HashMap<>();
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        params.put(i, args[i]);
      }
    }
    ctx.setInputParameters(params);
    OInternalExecutionPlan executionPlan;
    if (usePlanCache) {
      executionPlan = createExecutionPlan(ctx, false);
    } else {
      executionPlan = createExecutionPlanNoCache(ctx, false);
    }

    return new OLocalResultSet(executionPlan);
  }

  @Override
  public OResultSet execute(
      ODatabase db, Map params, OCommandContext parentCtx, boolean usePlanCache) {
    OBasicCommandContext ctx = new OBasicCommandContext();
    if (parentCtx != null) {
      ctx.setParentWithoutOverridingChild(parentCtx);
    }
    ctx.setDatabase(db);
    ctx.setInputParameters(params);
    OInternalExecutionPlan executionPlan;
    if (usePlanCache) {
      executionPlan = createExecutionPlan(ctx, false);
    } else {
      executionPlan = createExecutionPlanNoCache(ctx, false);
    }

    return new OLocalResultSet(executionPlan);
  }

  @Override
  public OInternalExecutionPlan createExecutionPlan(OCommandContext ctx, boolean enableProfiling) {
    return new OFindReferencesExecutionPlanner(this).createExecutionPlan(ctx, enableProfiling);
  }

  @Override
  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append("FIND REFERENCES ");
    if (rid != null) {
      rid.toString(params, builder);
    } else {
      builder.append(" ( ");
      subQuery.toString(params, builder);
      builder.append(" )");
    }
    if (targets != null) {
      builder.append(" [");
      boolean first = true;
      for (SimpleNode node : targets) {
        if (!first) {
          builder.append(",");
        }
        node.toString(params, builder);
        first = false;
      }
      builder.append("]");
    }
  }

  @Override
  public void toGenericStatement(StringBuilder builder) {
    builder.append("FIND REFERENCES ");
    if (rid != null) {
      rid.toGenericStatement(builder);
    } else {
      builder.append(" ( ");
      subQuery.toGenericStatement(builder);
      builder.append(" )");
    }
    if (targets != null) {
      builder.append(" [");
      boolean first = true;
      for (SimpleNode node : targets) {
        if (!first) {
          builder.append(",");
        }
        node.toGenericStatement(builder);
        first = false;
      }
      builder.append("]");
    }
  }

  @Override
  public OFindReferencesStatement copy() {
    OFindReferencesStatement result = new OFindReferencesStatement(-1);
    result.rid = rid == null ? null : rid.copy();
    result.subQuery = subQuery == null ? null : subQuery.copy();
    result.targets =
        targets == null ? null : targets.stream().map(x -> x.copy()).collect(Collectors.toList());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OFindReferencesStatement that = (OFindReferencesStatement) o;

    if (rid != null ? !rid.equals(that.rid) : that.rid != null) return false;
    if (subQuery != null ? !subQuery.equals(that.subQuery) : that.subQuery != null) return false;
    if (targets != null ? !targets.equals(that.targets) : that.targets != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = rid != null ? rid.hashCode() : 0;
    result = 31 * result + (subQuery != null ? subQuery.hashCode() : 0);
    result = 31 * result + (targets != null ? targets.hashCode() : 0);
    return result;
  }

  public ORid getRid() {
    return rid;
  }

  public OStatement getSubQuery() {
    return subQuery;
  }

  public List<SimpleNode> getTargets() {
    return targets;
  }
}
/* JavaCC - OriginalChecksum=be781e05acef94aa5edd7438b4ead6d5 (do not edit this line) */
