package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글작성
    public void write(Board board, MultipartFile file) throws Exception{
        //프로젝트 저장경로를 지정하고
        //static.files에 저장시, 업로드할 때마다 서버를 재부팅해야하므로 main.webapp폴더를 따로 만듦
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\webapp";

        UUID uuid = UUID.randomUUID();//랜덤이름생성
        String fileName = uuid + "_" + file.getOriginalFilename();

        //파일을 담을 껍데기를 만들고
        File saveFile = new File(projectPath, fileName);//이부분, childname에 계속 ""를 붙여서 에러남.
        //껍데기에 파일을 담는다
        file.transferTo(saveFile);

        //db에 파일이름과 파일경로 저장
        board.setFilename(fileName);
        board.setFilepath("/webapp/" + fileName);

        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){//매개변수에 pageable 넣어주면서 반환값도 List에서 Page로 변경한다

        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){

        boardRepository.deleteById(id);
    }
}
