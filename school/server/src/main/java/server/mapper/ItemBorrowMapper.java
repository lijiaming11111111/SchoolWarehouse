package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.item.borrow.PageSelectItemBorrowDTO;
import com.school.entity.ItemBorrow;
import com.school.vo.item.borrow.PageSelectItemBorrowVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemBorrowMapper {
    @Insert("insert into item_borrow (id, item_id, borrower_user_name, borrower_user_id," +
            " department_id, quantity, borrow_time, return_time, actual_return_time, " +
            "status, remark) " +
            "values (#{id}, #{itemId}, #{borrowerUserName}, #{borrowerUserId}, #{departmentId}, " +
            "#{quantity}, #{borrowTime}, #{returnTime}, #{actualReturnTime}, #{status}, " +
            "#{remark})")
    void insertItemBorrow(ItemBorrow itemBorrow);

    @Select("select * from item_borrow where id = #{id}")
    ItemBorrow selectById(String id);

    void updateItemBorrow(ItemBorrow borrow);

    Page<PageSelectItemBorrowVO> pageQueryItemBorrow(PageSelectItemBorrowDTO pageSelectItemBorrowDTO);
}
