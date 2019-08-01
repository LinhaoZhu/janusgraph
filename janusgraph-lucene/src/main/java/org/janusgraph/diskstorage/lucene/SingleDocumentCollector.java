// Copyright 2019 JanusGraph Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.janusgraph.diskstorage.lucene;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.SimpleCollector;

public class SingleDocumentCollector extends SimpleCollector {
    int docId;
    int numDocs = 0;
    private int base = 0;

    public void collect(int doc) {
        this.numDocs++;
        this.docId = doc + this.base;
    }

    protected void doSetNextReader(LeafReaderContext context) {
        this.base = context.docBase;
    }

    public boolean needsScores() {
        return false;
    }
}
