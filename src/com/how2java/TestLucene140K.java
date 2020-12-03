/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: TestLucene140K
 * Author:   苏晨宇
 * Date:     2020/12/2 11:04
 * Description: 测试14万条数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.how2java;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.sample.LuceneIndexAndSearchDemo;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 〈一句话功能简述〉<br>
 * 〈测试14万条数据〉
 *
 * @author 苏晨宇
 * @create 2020/12/2
 * @since 1.0.0
 */
public class TestLucene140K {
    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        //1.准备中文分词器
        IKAnalyzer analyzer = new IKAnalyzer();
        //2.索引
        Directory index = createIndex(analyzer);



        //3.查询器

        //更新索引
        IndexWriterConfig config=new IndexWriterConfig(analyzer);
        IndexWriter indexWriter=new IndexWriter(index,config);
        Document doc=new Document();
        doc.add(new TextField("id","51173", Field.Store.YES));
        doc.add(new TextField("name","神鞭，鞭没了，神还在", Field.Store.YES));
        doc.add(new TextField("category","道具", Field.Store.YES));
        doc.add(new TextField("price","998", Field.Store.YES));
        doc.add(new TextField("place","南海群岛", Field.Store.YES));
        doc.add(new TextField("code","888888", Field.Store.YES));
        indexWriter.updateDocument(new Term("id","51173"),doc);
        indexWriter.commit();
        indexWriter.close();

        Scanner s = new Scanner(System.in);


        //删除id=51173的数据
//        IndexWriterConfig config=new IndexWriterConfig(analyzer);
//        IndexWriter indexWriter=new IndexWriter(index,config);
//        indexWriter.deleteDocuments(new Term("id","51173"));
//        indexWriter.commit();
//        indexWriter.close();

        while (true) {
            System.out.println("请输入查询关键字： ");
            String keyword = s.nextLine();
            System.out.println("当前关键字是：" + keyword);
            Query query = new QueryParser("name", analyzer).parse(keyword);
            //4.搜索
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            int numberPage = 10;
            ScoreDoc[] hits = searcher.search(query, numberPage).scoreDocs;

//            int pageNow=1;
//            int pageSize=10;
//            ScoreDoc[] hits=pageSearch2(query,searcher,pageNow,pageSize);

            //5.显示查询结果
            showSearchResults(searcher, hits, query, analyzer);

            //6.关闭查询
            reader.close();

        }
    }

    private static ScoreDoc[] pageSearch1(Query query,IndexSearcher searcher,int pageNow,int pageSize) throws IOException {
        TopDocs topDocs=searcher.search(query,pageNow*pageSize);
        System.out.println("查询到的总条数\t"+topDocs.totalHits);
        ScoreDoc[] alllScores=topDocs.scoreDocs;

        List<ScoreDoc> hitScores=new ArrayList<>();
        int start=(pageNow-1)*pageSize;
        int end=pageNow*pageSize;
        for(int i=start;i<end;i++){
            hitScores.add(alllScores[i]);
        }
        ScoreDoc[] hits=hitScores.toArray(new ScoreDoc[]{});
        return hits;
    }

    private static ScoreDoc[] pageSearch2(Query query,IndexSearcher searcher,int pageNow,int pageSize) throws IOException {
        int start=(pageNow-1)*pageSize;
        if(start==0){
            TopDocs topDocs=searcher.search(query,pageNow*pageSize);
            return topDocs.scoreDocs;
        }

        TopDocs topDocs=searcher.search(query,start);
        ScoreDoc preScore=topDocs.scoreDocs[start-1];
        topDocs=searcher.searchAfter(preScore,query,pageSize);
        return topDocs.scoreDocs;
    }

    private static void showSearchResults(IndexSearcher searcher, ScoreDoc[] hits, Query query, IKAnalyzer analyzer) throws IOException, InvalidTokenOffsetsException {
        System.out.println("找到" + hits.length + " 个命中");
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

        System.out.println("找到" + hits.length + " 个命中");
        System.out.println("序号\t匹配度得分\t结果");
        for (int i = 0; i < hits.length; i++) {
            ScoreDoc scoreDoc = hits[i];
            int docId = scoreDoc.doc;
            Document d = searcher.doc(docId);
            List<IndexableField> fields = d.getFields();
            System.out.print((i + 1));
            System.out.print("\t" + scoreDoc.score);
            for (IndexableField f : fields) {
                if (f.name().equals("name")) {
                    TokenStream tokenStream = analyzer.tokenStream(f.name(), new StringReader(d.get(f.name())));
                    String fieldContent = highlighter.getBestFragment(tokenStream, d.get(f.name()));
                    System.out.print("\t" + fieldContent);
                } else {
                    System.out.print("\t" + d.get(f.name()));
                }
            }
            System.out.println("<br>");

        }

    }

    private static Directory createIndex(IKAnalyzer analyzer) throws IOException {
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);
        String fileName = "140k_products.txt";
        List<Product> products = ProductUtil.file2list(fileName);
        int total = products.size();
        int count = 0;
        int per = 0;
        int oldPer = 0;

        for (Product p : products) {
            addDoc(writer, p);
            count++;
            per = count * 100 / total;
            if (per != oldPer) {
                oldPer = per;
                System.out.printf("索引中，总共要添加%d条记录，当前添加进度是：%d%% %n", total, per);
            }

//            if(per>10)
//                break;
        }
        writer.close();
        return index;
    }

    private static void addDoc(IndexWriter w, Product p) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("id", String.valueOf(p.getId()), Field.Store.YES));
        doc.add(new TextField("name", p.getName(), Field.Store.YES));
        doc.add(new TextField("category", p.getCategory(), Field.Store.YES));
        doc.add(new TextField("price", String.valueOf(p.getPrice()), Field.Store.YES));
        doc.add(new TextField("place", p.getPlace(), Field.Store.YES));
        doc.add(new TextField("code", p.getCode(), Field.Store.YES));
        w.addDocument(doc);

    }
}
 
