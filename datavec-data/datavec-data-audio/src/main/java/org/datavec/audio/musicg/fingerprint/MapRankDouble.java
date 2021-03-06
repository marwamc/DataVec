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

package org.datavec.audio.musicg.fingerprint;

import java.util.*;
import java.util.Map.Entry;

public class MapRankDouble implements MapRank{
	
	private Map map;
	private boolean ascending=true;
	
	public MapRankDouble(Map<?,Double> map, boolean ascending){
		this.map=map;
		this.ascending=ascending;
	}
	
	public List getOrderedKeyList(int numKeys, boolean sharpLimit){	// if sharp limited, will return sharp numKeys, otherwise will return until the values not equals the exact key's value
				
		Set mapEntrySet=map.entrySet();
		List keyList=new LinkedList();
		
		// if the numKeys is larger than map size, limit it
		if (numKeys>map.size()){
			numKeys=map.size();
		}
		// end if the numKeys is larger than map size, limit it
		
		if (map.size()>0){
			double[] array=new double[map.size()];
			int count=0;
			
			// get the pass values
			Iterator<Entry> mapIterator=mapEntrySet.iterator();
			while (mapIterator.hasNext()){
				Entry entry=mapIterator.next();
				array[count++]=(Double)entry.getValue();
			}
			// end get the pass values
			
			int targetindex;
			if (ascending){
				targetindex=numKeys;
			}
			else{
				targetindex=array.length-numKeys;
			}
			
			double passValue=getOrderedValue(array,targetindex);	// this value is the value of the numKey-th element
			// get the passed keys and values
			Map passedMap=new HashMap();
			List<Double> valueList=new LinkedList<>();
			mapIterator=mapEntrySet.iterator();
			
			while (mapIterator.hasNext()){
				Entry entry=mapIterator.next();
				double value=(Double)entry.getValue();
				if ((ascending && value<=passValue) || (!ascending && value>=passValue)){
					passedMap.put(entry.getKey(), value);
					valueList.add(value);
				}
			}
			// end get the passed keys and values
			
			// sort the value list
			Double[] listArr=new Double[valueList.size()];
			valueList.toArray(listArr);
			Arrays.sort(listArr);
			// end sort the value list
			
			// get the list of keys
			int resultCount=0;
			int index;
			if (ascending){
				index=0;
			}
			else{
				index=listArr.length-1;
			}
			
			if (!sharpLimit){
				numKeys=listArr.length;
			}
			
			while (true){
				double targetValue=(Double)listArr[index];
				Iterator<Entry> passedMapIterator=passedMap.entrySet().iterator();
				while(passedMapIterator.hasNext()){
					Entry entry=passedMapIterator.next();
					if ((Double)entry.getValue()==targetValue){
						keyList.add(entry.getKey());
						passedMapIterator.remove();
						resultCount++;
						break;
					}
				}
				
				if (ascending){
					index++;
				}
				else{
					index--;
				}
				
				if (resultCount>=numKeys){
					break;
				}
			}
			// end get the list of keys
		}
		
		return keyList;
	}
	
	private double getOrderedValue(double[] array, int index){
		locate(array,0,array.length-1,index);
		return array[index];
	}
	
	// sort the partitions by quick sort, and locate the target index
	private void locate(double[] array, int left, int right, int index) {
        
		int mid=(left+right)/2;
		//System.out.println(left+" to "+right+" ("+mid+")");
		
		if (right==left){
			//System.out.println("* "+array[targetIndex]);
			//result=array[targetIndex];
			return;
		}
		
		if(left < right) { 
			double s = array[mid]; 
            int i = left - 1; 
            int j = right + 1; 

            while(true) { 
                while(array[++i] < s) ;
                while(array[--j] > s) ; 
                if(i >= j) 
                    break; 
                swap(array, i, j); 
            } 
            
            //System.out.println("2 parts: "+left+"-"+(i-1)+" and "+(j+1)+"-"+right);
            
            if (i>index){
            	// the target index in the left partition
            	//System.out.println("left partition");
            	locate(array, left, i-1,index);
            }
            else{
            	// the target index in the right partition
            	//System.out.println("right partition");
            	locate(array, j+1, right,index);     
            }
        }
    }
	
	private void swap(double[] array, int i, int j) {
		double t = array[i]; 
        array[i] = array[j]; 
        array[j] = t;
    }
}