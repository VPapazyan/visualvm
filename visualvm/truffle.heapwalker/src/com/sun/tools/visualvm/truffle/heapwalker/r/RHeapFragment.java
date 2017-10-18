/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.tools.visualvm.truffle.heapwalker.r;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;
import org.netbeans.modules.profiler.heapwalker.v2.HeapContext;
import org.netbeans.modules.profiler.heapwalker.v2.HeapFragment;
import com.sun.tools.visualvm.truffle.heapwalker.TruffleLanguageHeapFragment;
import com.sun.tools.visualvm.truffle.heapwalker.TruffleLanguageSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jiri Sedlacek
 */
class RHeapFragment extends TruffleLanguageHeapFragment {
    
    private static final String R_HEAP_ID = "r_heap";
    
    
    RHeapFragment(Instance langInfo, Heap heap) throws IOException {
        super("R Heap", R_HEAP_ID, langInfo, heap);
    }
    
    
    static boolean isRHeap(HeapContext context) {
        return R_HEAP_ID.equals(context.getFragment().getID()); // NOI18N
    }
    
    static RHeapFragment heap(Heap heap) {
        return (RHeapFragment)heap;
    }
    
    
    Iterator<Instance> getRObjectsIterator() {
        return getInstancesIterator(RObject.R_OBJECT_FQN);
    }
    
    
    @ServiceProvider(service=HeapFragment.Provider.class, position = 400)
    public static class Provider extends HeapFragment.Provider {

        private static final String R_LANG_ID = "R";  // NOI18N

        public HeapFragment getFragment(File heapDumpFile, Lookup.Provider heapDumpProject, Heap heap) throws IOException {
            Instance langInfo = TruffleLanguageSupport.getLanguageInfo(heap, R_LANG_ID);
            JavaClass RMainClass = heap.getJavaClassByName(RObject.R_OBJECT_FQN);

            return langInfo != null && RMainClass != null ? new RHeapFragment(langInfo, heap) : null;
        }

    }
    
}