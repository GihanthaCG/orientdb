/* Generated By:JJTree: Do not edit this line. OGeOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */

/*
 *
 *  *  Copyright 2015 OrientDB LTD (info(at)orientdb.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://www.orientdb.com
 *
 */

package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.metadata.schema.OType;
import java.util.Map;

public class OGeOperator extends SimpleNode implements OBinaryCompareOperator {
  public OGeOperator(int id) {
    super(id);
  }

  public OGeOperator(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public boolean execute(Object iLeft, Object iRight) {
    if (iLeft == iRight) {
      return true;
    }
    if (iLeft == null || iRight == null) {
      return false;
    }
    if (iLeft.getClass() != iRight.getClass()
        && iLeft instanceof Number
        && iRight instanceof Number) {
      Number[] couple = OType.castComparableNumber((Number) iLeft, (Number) iRight);
      iLeft = couple[0];
      iRight = couple[1];
    } else {
      iRight = OType.convert(iRight, iLeft.getClass());
    }
    if (iRight == null) return false;
    return ((Comparable<Object>) iLeft).compareTo(iRight) >= 0;
  }

  @Override
  public String toString() {
    return ">=";
  }

  @Override
  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append(">=");
  }

  @Override
  public void toGenericStatement(StringBuilder builder) {
    builder.append(">=");
  }

  @Override
  public boolean supportsBasicCalculation() {
    return true;
  }

  @Override
  public OGeOperator copy() {
    return this;
  }

  @Override
  public boolean isRangeOperator() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj.getClass().equals(this.getClass());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
/* JavaCC - OriginalChecksum=960da239569d393eb155f7d8a871e6d5 (do not edit this line) */
