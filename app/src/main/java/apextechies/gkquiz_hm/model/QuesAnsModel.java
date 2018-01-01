package apextechies.gkquiz_hm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shankar on 12/28/2017.
 */

public class QuesAnsModel implements Parcelable{


    protected QuesAnsModel(Parcel in) {
        ques_id = in.readString();
        subCatId = in.readString();
        question = in.readString();
        optionFirst = in.readString();
        optionSecond = in.readString();
        optionThird = in.readString();
        optionFourth = in.readString();
        answer = in.readString();
    }

    public static final Creator<QuesAnsModel> CREATOR = new Creator<QuesAnsModel>() {
        @Override
        public QuesAnsModel createFromParcel(Parcel in) {
            return new QuesAnsModel(in);
        }

        @Override
        public QuesAnsModel[] newArray(int size) {
            return new QuesAnsModel[size];
        }
    };

    public String getQues_id() {
        return ques_id;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionFirst() {
        return optionFirst;
    }

    public String getOptionSecond() {
        return optionSecond;
    }

    public String getOptionThird() {
        return optionThird;
    }

    public String getOptionFourth() {
        return optionFourth;
    }

    public String getAnswer() {
        return answer;
    }

    private String ques_id,subCatId,question,optionFirst,optionSecond,optionThird,optionFourth,answer;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ques_id);
        dest.writeString(subCatId);
        dest.writeString(question);
        dest.writeString(optionFirst);
        dest.writeString(optionSecond);
        dest.writeString(optionThird);
        dest.writeString(optionFourth);
        dest.writeString(answer);
    }
}
