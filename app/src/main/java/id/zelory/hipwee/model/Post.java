package id.zelory.hipwee.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zetbaitsu on 16/02/2015.
 */
public class Post implements Parcelable
{
    public static final String TOP_7 = "http://www.hipwee.com/top/?sort=7";
    public static final String TOP_30 = "http://www.hipwee.com/top/?sort=30";
    public static final String TOP_ALL = "http://www.hipwee.com/top/";
    public static final String HIBURAN = "http://www.hipwee.com/category/hiburan/page/";
    public static final String TIPS = "http://www.hipwee.com/category/tips/page/";
    public static final String HUBUNGAN = "http://www.hipwee.com/category/hubungan/page/";
    public static final String MOTIVASI = "http://www.hipwee.com/category/motivasi/page/";
    public static final String SUKSES = "http://www.hipwee.com/category/sukses/page/";
    public static final String TRAVEL = "http://www.hipwee.com/category/travel/page/";
    public static final String FEATURE = "http://www.hipwee.com/category/feature/page/";
    public static final String STYLE = "http://www.hipwee.com/category/style/page/";
    public static final String SEARCH = "http://www.hipwee.com/?s=";
    
    public static final String URL_HIBURAN = "http://www.hipwee.com/category/hiburan";
    public static final String URL_TIPS = "http://www.hipwee.com/category/tips";
    public static final String URL_HUBUNGAN = "http://www.hipwee.com/category/hubungan";
    public static final String URL_MOTIVASI = "http://www.hipwee.com/category/motivasi";
    public static final String URL_SUKSES = "http://www.hipwee.com/category/sukses";
    public static final String URL_TRAVEL = "http://www.hipwee.com/category/travel";
    public static final String URL_FEATURE = "http://www.hipwee.com/category/feature";
    public static final String URL_STYLE = "http://www.hipwee.com/category/style";

    public static final String ARTIKEL_HIBURAN = "http://www.hipwee.com/hiburan/";
    public static final String ARTIKEL_TIPS = "http://www.hipwee.com/tips/";
    public static final String ARTIKEL_HUBUNGAN = "http://www.hipwee.com/hubungan/";
    public static final String ARTIKEL_MOTIVASI = "http://www.hipwee.com/motivasi/";
    public static final String ARTIKEL_SUKSES = "http://www.hipwee.com/sukses/";
    public static final String ARTIKEL_TRAVEL = "http://www.hipwee.com/travel/";
    public static final String ARTIKEL_FEATURE = "http://www.hipwee.com/feature/";
    public static final String ARTIKEL_STYLE = "http://www.hipwee.com/style/";
    
    private String judul;
    private String deskripsi;
    private String gambar;
    private String alamat;

    public String getJudul()
    {
        return judul;
    }

    public void setJudul(String judul)
    {
        this.judul = judul;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public String getGambar()
    {
        return gambar;
    }

    public void setGambar(String gambar)
    {
        this.gambar = gambar;
    }

    public String getAlamat()
    {
        return alamat;
    }

    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(judul);
        dest.writeString(deskripsi);
        dest.writeString(gambar);
        dest.writeString(alamat);
    }

    public static final Parcelable.Creator<Post> CREATOR
            = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    
    public Post()
    {

    }
    private Post(Parcel in)
    {
        judul = in.readString();
        deskripsi = in.readString();
        gambar = in.readString();
        alamat = in.readString();
    }
}
