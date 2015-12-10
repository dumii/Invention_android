package fr.afcepf.al25.invention_v1;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductDTO implements Parcelable{
    private Integer idProduct;
    private String nomProduct;
    private Date dateCreationProduct;
    private String photo;
    private Integer categorieProduct;

    public ProductDTO() {}

    public ProductDTO(Integer idProduct, String nomProduct, Date dateCreationProduct, String photo, Integer categorieProduct) {
        this.idProduct = idProduct;
        this.nomProduct = nomProduct;
        this.dateCreationProduct = dateCreationProduct;
        this.photo = photo;
        this.categorieProduct = categorieProduct;
    }

    public Integer getIdProduct() {
        return idProduct;
    }
    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }
    public String getNomProduct() {
        return nomProduct;
    }
    public void setNomProduct(String nomProduct) {
        this.nomProduct = nomProduct;
    }
    public Date getDateCreationProduct() {
        return dateCreationProduct;
    }
    public void setDateCreationProduct(Date dateCreationProduct) {
        this.dateCreationProduct = dateCreationProduct;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public Integer getCategorieProduct() {
        return categorieProduct;
    }
    public void setCategorieProduct(Integer categorieProduct) {
        this.categorieProduct = categorieProduct;
    }

    //parcel part
    public ProductDTO(Parcel in){
        String[] data= new String[5];

        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = shortenedDateFormat.parse(data[2]);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }

        in.readStringArray(data);
        this.idProduct = Integer.parseInt(data[0]);
        this.nomProduct = data[1];
        this.dateCreationProduct = date;
        this.photo = data[3];
        this.categorieProduct = Integer.parseInt(data[4]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateCreationProductStr = formatter.format(this.dateCreationProduct);
        dest.writeStringArray(
                new String[]{
                        String.valueOf(this.idProduct),
                        this.nomProduct,
                        dateCreationProductStr,
                        this.photo,
                        String.valueOf(this.categorieProduct)
                });
    }

    public static final Parcelable.Creator<ProductDTO> CREATOR= new Parcelable.Creator<ProductDTO>() {

        @Override
        public ProductDTO createFromParcel(Parcel source) {
// TODO Auto-generated method stub
            return new ProductDTO(source);  //using parcelable constructor
        }

        @Override
        public ProductDTO[] newArray(int size) {
// TODO Auto-generated method stub
            return new ProductDTO[size];
        }
    };
}
