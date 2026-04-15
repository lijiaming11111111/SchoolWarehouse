package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.item.category.PageSelectItemCategoryDTO;
import com.school.dto.item.category.SelectItemCategoryTreeDTO;
import com.school.entity.ItemCategory;
import com.school.vo.item.category.PageSelectItemCategoryVO;
import com.school.vo.item.category.SelectItemCategoryTreeVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemCategoryMapper {

    @Insert("insert into item_category (id, item_category_name, parent_id) values (#{id}, #{itemCategoryName}, #{parentId})")
    void insertItemCategory(ItemCategory itemCategory);

    @Select("select * from item_category where id = #{id}")
    ItemCategory selectItemCategoryById(String id);

    @Update("update item_category set item_category_name = #{itemCategoryName}, parent_id = #{parentId} where id = #{id}")
    void updateItemCategory(ItemCategory itemCategory);

    @Delete("delete from item_category where id = #{id}")
    void deleteItemCategoryById(String id);

    Page<PageSelectItemCategoryVO> pageQueryItemCategory(PageSelectItemCategoryDTO pageSelectItemCategoryDTO);

    List<SelectItemCategoryTreeVO> selectItemCategoryTree();
}
