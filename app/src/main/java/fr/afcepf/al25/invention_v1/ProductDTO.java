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
    private String descriptionProduct;
    private Double prixProduct;

    public ProductDTO() {}

    public ProductDTO(Integer idProduct, String nomProduct, Date dateCreationProduct,
                      String photo, Integer categorieProduct, String descriptionProduct, Double prixProduct) {
        this.idProduct = idProduct;
        this.nomProduct = nomProduct;
        this.dateCreationProduct = dateCreationProduct;
        this.photo = photo;
        this.categorieProduct = categorieProduct;
        this.descriptionProduct = descriptionProduct;
        this.prixProduct = prixProduct;
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

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public Double getPrixProduct() {
        return prixProduct;
    }

    public void setPrixProduct(Double prixProduct) {
        this.prixProduct = prixProduct;
    }

    //parcel part
    public ProductDTO(Parcel in){
        String[] data= new String[7];

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
        this.descriptionProduct = data[5];
        if(data[6]!=null){
        this.prixProduct = Double.parseDouble(data[6]);}
        else {
            this.prixProduct = 0.00;
        }
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
                        String.valueOf(this.categorieProduct),
                        this.descriptionProduct,
                        String.valueOf(this.prixProduct)
                });
    }

    public static final Parcelable.Creator<ProductDTO> CREATOR = new Parcelable.Creator<ProductDTO>() {

        @Override
        public ProductDTO createFromParcel(Parcel source) {
            return new ProductDTO(source);  //using parcelable constructor
        }

        @Override
        public ProductDTO[] newArray(int size) {
            return new ProductDTO[size];
        }
    };
}
