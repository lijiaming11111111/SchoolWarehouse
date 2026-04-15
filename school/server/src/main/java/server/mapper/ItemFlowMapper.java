package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.item.flow.PageSelectItemFlowDTO;
import com.school.vo.item.flow.PageSelectItemFlowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemFlowMapper {
    Page<PageSelectItemFlowVO> pageQueryItemFlow(PageSelectItemFlowDTO pageSelectItemFlowDTO);
}
