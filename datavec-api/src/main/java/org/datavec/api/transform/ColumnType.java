/*
 *  * Copyright 2016 Skymind, Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 */

package org.datavec.api.transform;

import org.datavec.api.transform.metadata.*;

/**
 * The type of column.
 */
public enum ColumnType {
    String,
    Integer,
    Long,
    Double,
    Float,
    Categorical,
    Time,
    Bytes, //Arbitrary byte[] data
    Boolean;

    public ColumnMetaData newColumnMetaData(String columnName){
        switch (this){
            case String:
                return new StringMetaData(columnName);
            case Integer:
                return new IntegerMetaData(columnName);
            case Long:
                return new LongMetaData(columnName);
            case Double:
                return new DoubleMetaData(columnName);
            case Float:
                return new FloatMetaData(columnName);
            case Time:
                return new TimeMetaData(columnName);
            case Boolean:
                return new CategoricalMetaData(columnName, "true", "false");
            case Categorical:
                throw new UnsupportedOperationException("Cannot create new categorical column using this method: categorical state names would be unknown");
            default: //And Bytes
                throw new UnsupportedOperationException("Unknown or not supported column type: " + this);
        }
    }

}
