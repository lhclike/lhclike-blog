package com.lhclike.myblog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class Article {
    public static  final int Article_TOP=1;
    public static  final  int Article_Common=0;

    @TableId(value = "id",type= IdType.AUTO)
    private Long id;
    private  String title;
    private String summary;

    private Integer commentCounts;
    private Integer viewCounts;

    //作者id
    private Long authorId;
    //内容id
    private Long bodyId;

    //类别id
    private Long categoryId;

    //置顶
    private int weight=Article_Common;
    //创建时间
    private Long createDate;
}
