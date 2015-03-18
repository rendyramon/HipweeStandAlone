package id.zelory.hipwee.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.model.PostDetail;

/**
 * Created by zetbaitsu on 03/03/2015.
 */
public class Scraper
{
    public static final String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";

    public static ArrayList<Post> scrapThis(String url)
    {
        final ArrayList<Post> posts = new ArrayList<>();
        Document document = null;
        Elements elements;

        if (url.equals(Post.TOP_7) || url.equals(Post.TOP_30) || url.equals(Post.TOP_ALL))
        {
            while (document == null)
            {
                try
                {
                    document = Jsoup.connect(url).userAgent(userAgent).get();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (document != null)
                {
                    elements = document.select("h1");
                    for (int i = 0; i < elements.size(); i++)
                    {
                        Element element = elements.get(i);
                        Post post = new Post();
                        Document tmp = new Document("");
                        String html = element.text();
                        tmp.append(html);
                        tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                        tmp.outputSettings().charset("ASCII");
                        post.setJudul(tmp.toString());
                        posts.add(post);
                    }

                    final Document temp = document;
                    new Thread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            Elements elements = temp
                                    .select("img.attachment-front_thumb.wp-post-image");
                            if (elements.size() > 10)
                            {
                                for (int i = 1; i < elements.size(); i = i + 2)
                                {
                                    Element element = elements.get(i);
                                    posts.get(i / 2).setGambar(
                                            element.attr("src"));
                                }
                            } else if (elements.size() == 10)
                            {
                                for (int i = 0; i < elements.size(); i++)
                                {
                                    Element element = elements.get(i);
                                    posts.get(i).setGambar(element.attr("src"));
                                }
                            }
                        }
                    }).start();

                    elements = document.select("div.index-title a");
                    for (int i = 0; i < elements.size(); i = i + 2)
                    {
                        Element element = elements.get(i);
                        posts.get(i / 2).setAlamat(element.attr("href"));
                    }

                }
            }
        } else
        {
            Holder holder = getDocuments(url);

            switch (url)
            {
                case Post.FEATURE:
                    while (holder.thread[0].isAlive() || holder.thread[1].isAlive()
                            || holder.thread[2].isAlive()) ;
                    break;
                case Post.STYLE:
                    while (holder.thread[0].isAlive() || holder.thread[1].isAlive()) ;
                    break;
                default:
                    while (holder.thread[0].isAlive() || holder.thread[1].isAlive()
                            || holder.thread[2].isAlive()
                            || holder.thread[3].isAlive()
                            || holder.thread[4].isAlive()) ;
                    break;
            }

            for (int i = 0; i < 5; i++)
            {
                if (holder.documents[i] == null)
                    i++;
                if (i == 5)
                    break;

                Document document2 = holder.documents[i];
                Element element = document2.select("h1").first();
                Post post = new Post();
                Document tmp = new Document("");
                String html = element.text();
                tmp.append(html);
                tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                tmp.outputSettings().charset("ASCII");
                post.setJudul(tmp.toString());

                element = document2.select(
                        "img.attachment-front_thumb.wp-post-image").first();
                post.setGambar(element.attr("src"));
                element = document2.select("div.index-title a").first();
                post.setAlamat(element.attr("href"));
                post.setDeskripsi("");
                posts.add(post);
                final int k = posts.size();

                elements = document2.select("div.col-sm-8 h3");
                for (int j = 0; j < elements.size(); j++)
                {
                    element = elements.get(j);
                    post = new Post();
                    tmp = new Document("");
                    html = element.text();
                    tmp.append(html);
                    tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                    tmp.outputSettings().charset("ASCII");
                    post.setJudul(tmp.toString());
                    posts.add(post);
                }

                final Document temp = document2;
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Elements elements = temp
                                .select("article.row.article-front-next img.attachment-sec_thumb.wp-post-image");
                        for (int j = 0; j < elements.size(); j++)
                        {
                            Element element = elements.get(j);
                            posts.get(j + k).setGambar(element.attr("src"));

                        }
                    }
                }).start();

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Elements elements = temp.select("div.col-sm-8 a");
                        Elements tmp = new Elements();
                        for (int j = 0; j < elements.size(); j++)
                        {
                            if (j % 2 == 0)
                                tmp.add(elements.get(j));
                        }
                        for (int j = 0; j < tmp.size(); j++)
                        {
                            Element element = tmp.get(j);
                            posts.get(j + k).setAlamat(element.attr("href"));

                        }
                    }
                }).start();

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Elements elements = temp.select("div.col-sm-8 p");
                        for (int j = 0; j < elements.size(); j++)
                        {
                            Element element = elements.get(j);
                            Document tmp = new Document("");
                            String html = element.text();
                            tmp.append(html);
                            tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                            tmp.outputSettings().charset("ASCII");
                            posts.get(j + k).setDeskripsi(tmp.toString());

                        }
                    }
                }).start();

                if (i == 2 && url.equals(Post.FEATURE))
                    break;
                else if (i == 1 && url.equals(Post.STYLE))
                    break;
            }

        }

        return posts;

    }

    public static ArrayList<Post> search(String keyword)
    {
        ArrayList<Post> post = new ArrayList<>();
        Document document = null;
        boolean gagal = false;
        try
        {
            document = Jsoup.connect(Post.SEARCH + keyword).userAgent(userAgent).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (document != null)
        {
            String h2 = null;
            try
            {
                h2 = document.select("h2").first().text();
            } catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            if (h2 != null && h2.equals("Nothing Found"))
            {
                gagal = true;
            }
            if (!gagal)
            {
                boolean masih = true;
                Elements elements = document
                        .select("div.col-xs-12.text-center");
                Element element = elements.first();
                if (element.text().equals(""))
                    masih = false;
                int i = 1;
                while (true)
                {
                    try
                    {
                        document = Jsoup
                                .connect("http://www.hipwee.com/page/" + i
                                        + "/?s=" + keyword).userAgent(userAgent).get();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if (document != null)
                    {
                        element = document.select("h1").first();
                        Post hipwee = new Post();
                        Document tmp = new Document("");
                        String html = element.text();
                        tmp.append(html);
                        tmp.outputSettings().escapeMode(
                                Entities.EscapeMode.extended);
                        tmp.outputSettings().charset("ASCII");
                        hipwee.setJudul(tmp.toString());

                        element = document.select(
                                "img.attachment-front_thumb.wp-post-image")
                                .first();
                        hipwee.setGambar(element.attr("src"));
                        element = document.select("div.index-title a").first();
                        hipwee.setAlamat(element.attr("href"));
                        hipwee.setDeskripsi("");
                        post.add(hipwee);
                        int k = post.size();

                        elements = document.select("div.col-sm-8 h3");
                        for (int j = 0; j < elements.size(); j++)
                        {
                            element = elements.get(j);
                            hipwee = new Post();
                            tmp = new Document("");
                            html = element.text();
                            tmp.append(html);
                            tmp.outputSettings().escapeMode(
                                    Entities.EscapeMode.extended);
                            tmp.outputSettings().charset("ASCII");
                            hipwee.setJudul(tmp.toString());
                            post.add(hipwee);
                        }

                        elements = document
                                .select("article.row.article-front-next img.attachment-sec_thumb.wp-post-image");
                        for (int j = 0; j < elements.size(); j++)
                        {
                            element = elements.get(j);
                            post.get(j + k).setGambar(element.attr("src"));
                        }

                        elements = document.select("div.col-sm-8 a");
                        Elements tmps = new Elements();
                        for (int j = 0; j < elements.size(); j++)
                        {
                            if (j % 2 == 0)
                                tmps.add(elements.get(j));
                        }
                        for (int j = 0; j < tmps.size(); j++)
                        {
                            element = tmps.get(j);

                            if (element.attr("href").contains("hipwee.com"))
                                post.get(j + k).setAlamat(element.attr("href"));
                            else
                            {
                                tmps.remove(j);
                                j--;
                            }
                        }

                        elements = document.select("div.col-sm-8 p");
                        for (int j = 0; j < elements.size(); j++)
                        {
                            element = elements.get(j);
                            if (!element
                                    .toString()
                                    .contains("<small><strong>Content partner</strong></small>"))
                            {
                                tmp = new Document("");
                                html = element.text();
                                tmp.append(html);
                                tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                                tmp.outputSettings().charset("ASCII");
                                post.get(j + k).setDeskripsi(tmp.toString());
                            } else
                            {
                                elements.remove(j);
                                elements.remove(j);
                                j--;
                            }
                        }
                    }
                    i++;
                    if (i > 2 || !masih)
                        break;
                }
            }
        }
        if (gagal)
        {
            Post hipwee = new Post();
            hipwee.setJudul("Pencarian artikel tidak berhasil ditemukan, silahkan coba lagi dengan kata kunci lain.");
            hipwee.setDeskripsi("");
            hipwee.setGambar("http://zeloryapi.appspot.com/error.png");
            hipwee.setAlamat("");
            post.add(hipwee);
        }
        return post;
    }

    public static PostDetail read(String url)
    {
        PostDetail post = new PostDetail();
        Document document = null;

        while (document == null)
        {
            try
            {
                document = Jsoup.connect(url).userAgent(userAgent).get();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if (document != null)
            {
                Document tmp = new Document("");
                String html;

                Element element = document.select("h1").first();
                html = element.text();
                tmp.append(html);
                tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                tmp.outputSettings().charset("ASCII");
                post.setJudul(tmp.toString());

                element = document.select("span.post-date").first();
                post.setTanggal(element.text());

                element = document.select("strong").first();
                post.setPenulis(element.text());

                element = document.select("div#entry-content").first();
                html = element.html();
                Log.d("zet", html);
                try
                {
                    html = html.substring(html.indexOf("<p"));
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                tmp = new Document("");
                tmp.append(html);
                tmp.outputSettings().escapeMode(Entities.EscapeMode.extended);
                tmp.outputSettings().charset("ASCII");
                post.setIsi(tmp.toString());
            }
        }
        return post;
    }

    public static ArrayList<Post> random()
    {
        ArrayList<Post> post = new ArrayList<>();
        String url = null;
        Document document = null;
        int cat = Utils.randInt(4, 11);

        switch (cat)
        {
            case 4:
                url = Post.HIBURAN;
                break;
            case 5:
                url = Post.TIPS;
                break;
            case 6:
                url = Post.HUBUNGAN;
                break;
            case 7:
                url = Post.MOTIVASI;
                break;
            case 8:
                url = Post.SUKSES;
                break;
            case 9:
                url = Post.TRAVEL;
                break;
            case 10:
                url = Post.FEATURE;
                break;
            case 11:
                url = Post.STYLE;
                break;
            default:
                break;
        }

        while (document == null)
        {
            try
            {
                document = Jsoup.connect(url + "1").userAgent(userAgent).get();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            if (document != null)
            {
                Elements elements = document.select("div.col-sm-8 h3");
                Element element;
                for (int i = 0; i < elements.size(); i++)
                {
                    element = elements.get(i);
                    Post hipwee = new Post();
                    Document tmp = new Document("");
                    String html = element.text();
                    tmp.append(html);
                    tmp.outputSettings().escapeMode(
                            Entities.EscapeMode.extended);
                    tmp.outputSettings().charset("ASCII");
                    hipwee.setJudul(tmp.toString());
                    post.add(hipwee);
                }

                elements = document
                        .select("article.row.article-front-next img.attachment-sec_thumb.wp-post-image");
                for (int i = 0; i < elements.size(); i++)
                {
                    element = elements.get(i);
                    post.get(i).setGambar(element.attr("src"));
                }

                elements = document.select("div.col-sm-8 a");
                Elements tmp = new Elements();
                for (int i = 0; i < elements.size(); i++)
                {
                    if (i % 2 == 0)
                        tmp.add(elements.get(i));
                }
                for (int i = 0; i < tmp.size(); i++)
                {
                    element = tmp.get(i);
                    post.get(i).setAlamat(element.attr("href"));
                }

                elements = document.select("div.col-sm-8 p");
                for (int i = 0; i < elements.size(); i++)
                {
                    element = elements.get(i);
                    Document tmps = new Document("");
                    String html = element.text();
                    tmps.append(html);
                    tmps.outputSettings().escapeMode(
                            Entities.EscapeMode.extended);
                    tmps.outputSettings().charset("ASCII");
                    post.get(i).setDeskripsi(tmps.toString());
                }

            }
        }
        return post;
    }

    private static class Holder
    {
        Document documents[];
        Thread thread[];
    }

    private static Holder getDocuments(final String url)
    {
        final Holder holder = new Holder();
        holder.documents = new Document[5];
        holder.thread = new Thread[5];
        for (int i = 0; i < 5; i++)
        {
            final int x = i + 1;
            holder.thread[x - 1] = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Document document = null;
                    int j = 0;
                    while (document == null)
                    {
                        try
                        {
                            document = Jsoup.connect(url + x).userAgent(userAgent).get();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (document != null)
                        {
                            holder.documents[x - 1] = document;
                        }
                        j++;
                        if (j > 5)
                            break;
                    }
                }
            });
            holder.thread[x - 1].start();
            if (i == 2 && url.equals(Post.FEATURE))
                break;
            else if (i == 1 && url.equals(Post.STYLE))
                break;
        }

        return holder;
    }
}
