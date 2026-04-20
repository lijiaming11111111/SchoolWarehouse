package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.item.flow.PageSelectItemFlowDTO;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface ItemFlowMapper {
    Page<PageSelectItemFlowVO> pageQueryItemFlow(PageSelectItemFlowDTO pageSelectItemFlowDTO);

    @Update("UPDATE item_flow SET status = 3 WHERE status = 1 AND return_time < #{now}")
    void updateRegularCheckItemFlow(LocalDateTime now);
}
