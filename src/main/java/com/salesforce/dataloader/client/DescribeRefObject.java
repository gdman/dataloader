/*
 * Copyright (c) 2015, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.salesforce.dataloader.client;

import java.util.Map;

import com.sforce.soap.partner.Field;

/**
 * Container for reference object info. Right now, it's used for upsert references than need the set of external id
 * fields
 * 
 * @author Alex Warshavsky
 * @since 8.0
 */
public class DescribeRefObject {

    private String parentObjectName;
    private Map<String, Field> parentFieldInfoMap;
    private Field childField;
    public static final int MAX_PARENT_OBJECTS_IN_REFERENCING_FIELD = 5;

    DescribeRefObject(String parentObjectName, Field childField, Map<String,Field> fieldInfoMap) {
        this.parentObjectName = parentObjectName;
        this.parentFieldInfoMap = fieldInfoMap;
        this.childField = childField;
    }

    public Map<String, Field> getFieldInfoMap() {
        return parentFieldInfoMap;
    }

    public String getParentObjectName() {
        return parentObjectName;
    }
    
    public Field getChildField() {
        return childField;
    }
}
