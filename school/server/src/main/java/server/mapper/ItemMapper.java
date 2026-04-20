package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.item.PageSelectItemDTO;
import com.school.entity.ItemBorrow;
import com.school.entity.ItemFlow;
import com.school.enums.item.category.ItemStatus;
import com.school.vo.item.SelectItemVO;
import com.school.entity.Item;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ItemMapper {
    @Insert("insert into item (id, item_name, item_code, item_category_id, specification, unit, warehouse_id, " +
            "current_stock, safe_stock, buy_time, buy_price, use_department, status) " +
            "values (#{id}, #{itemName}, #{itemCode}, #{itemCategoryId}, #{specification}, #{unit}, #{warehouseId}," +
            " #{currentStock}, #{safeStock}, #{buyTime}, #{buyPrice}, #{useDepartment}, #{itemStatus})")
    void insertItem(Item item);

    Boolean updateItem(Item item);

    @Select("select * from item where id = #{id}")
    SelectItemVO selectItem(String id);

    @Delete("delete from item where id = #{id}")
    void deleteItem(String id);

    @Select("select count(*) from item where id = #{id}")
    Long selectCountById(String id);

    Page<SelectItemVO> pageQueryItem(PageSelectItemDTO pageSelectItemDTO);


    @Select("select current_stock from item where id = #{id}")
    Long selectCountItem(String id);

    @Insert("insert into item_flow (id, item_id, item_name, flow_type, quantity, before_stock, after_stock, remark,create_time) " +
            "values (#{id}, #{itemId}, #{itemName}, #{flowType}, #{quantity}, #{beforeStock}, #{afterStock}, #{remark}, #{createTime})")
    void insertItemFlow(ItemFlow itemFlow);

    @Select("select status from item where id = #{id}")
    ItemStatus selectItemStatus(String id);
}
