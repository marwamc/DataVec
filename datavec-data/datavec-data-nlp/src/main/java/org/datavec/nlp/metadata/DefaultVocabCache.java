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

package org.datavec.nlp.metadata;

import org.datavec.api.berkeley.Counter;
import org.datavec.api.conf.Configuration;
import org.datavec.api.util.Index;
import org.datavec.api.util.MathUtils;
import org.datavec.nlp.vectorizer.TextVectorizer;

/**
 * Vocab cache uswed for storing information
 * about vocab
 *
 * @author Adam Gibson
 */
public class DefaultVocabCache implements VocabCache {

    private Counter<String> wordFrequencies = new Counter<>();
    private Counter<String> docFrequencies = new Counter<>();
    private int minWordFrequency;
    private Index vocabWords = new Index();
    private double numDocs = 0;

    /**
     * Instantiate with a given min word frequency
     * @param minWordFrequency
     */
    public DefaultVocabCache(int minWordFrequency) {
        this.minWordFrequency = minWordFrequency;
    }

    /*
     * Constructor for use with initialize()
     */
    public DefaultVocabCache(){}

    @Override
    public void incrementNumDocs(double by) {
        numDocs += by;
    }

    @Override
    public double numDocs() {
        return numDocs;
    }

    @Override
    public String wordAt(int i) {
        return vocabWords.get(i).toString();
    }

    @Override
    public void initialize(Configuration conf) {
        minWordFrequency = conf.getInt(TextVectorizer.MIN_WORD_FREQUENCY,5);
    }

    @Override
    public double wordFrequency(String word) {
        return wordFrequencies.getCount(word);
    }

    @Override
    public int minWordFrequency() {
        return minWordFrequency;
    }

    @Override
    public Index vocabWords() {
        return vocabWords;
    }

    @Override
    public void incrementDocCount(String word) {
        incrementDocCount(word,1.0);
    }

    @Override
    public void incrementDocCount(String word, double by) {
        docFrequencies.incrementCount(word,by);

    }

    @Override
    public void incrementCount(String word) {
        incrementCount(word,1.0);
    }

    @Override
    public void incrementCount(String word, double by) {
        wordFrequencies.incrementCount(word,by);
        if(wordFrequencies.getCount(word) >= minWordFrequency && vocabWords.indexOf(word) < 0)
            vocabWords.add(word);
    }

    @Override
    public double idf(String word) {
        return docFrequencies.getCount(word);
    }

    @Override
    public double tfidf(String word, double frequency) {
        return MathUtils.tfidf(MathUtils.tf((int) frequency), MathUtils.idf(numDocs, idf(word)));
    }

    public int getMinWordFrequency() {
        return minWordFrequency;
    }

    public void setMinWordFrequency(int minWordFrequency) {
        this.minWordFrequency = minWordFrequency;
    }
}
