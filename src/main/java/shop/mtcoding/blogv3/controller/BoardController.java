package shop.mtcoding.blogv3.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import shop.mtcoding.blogv3.dto.ResponseDto;
import shop.mtcoding.blogv3.dto.board.BoardReqDto.BoardSaveReqDto;
import shop.mtcoding.blogv3.dto.board.BoardReqDto.BoardUpdateReqDto;
import shop.mtcoding.blogv3.dto.board.BoardResDto.BoardMainResponseDto;
import shop.mtcoding.blogv3.handler.ex.CustomApiException;
import shop.mtcoding.blogv3.handler.ex.CustomException;
import shop.mtcoding.blogv3.model.Board;
import shop.mtcoding.blogv3.model.BoardRepository;
import shop.mtcoding.blogv3.model.Like;
import shop.mtcoding.blogv3.model.LikeRepository;
import shop.mtcoding.blogv3.model.ReplyRepository;
import shop.mtcoding.blogv3.model.User;
import shop.mtcoding.blogv3.service.BoardService;

@Controller
public class BoardController {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardService boardService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/")
    public String main(Model model) {
        List<BoardMainResponseDto> dtos = boardRepository.findAllWithUser();
        model.addAttribute("dtos", dtos);
        return "board/main";
    }

    @GetMapping("/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @PostMapping("/board")
    public ResponseEntity<?> save(@RequestBody BoardSaveReqDto boardSaveReqDto) {
        User principal = (User) session.getAttribute("principal");

        if (principal == null) {
            throw new CustomException("?????? ?????? : ????????? ??????");
        }

        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("title??? ??????????????????");
        }
        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("Content??? ??????????????????");
        }
        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("title??? ????????? 100??? ???????????? ?????????");
        }

        boardService.save(boardSaveReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "????????? ?????? ??????", null), HttpStatus.CREATED);
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {

        model.addAttribute("boardDto", boardRepository.findByIdWithUser(id));
        model.addAttribute("replyDtos", replyRepository.findByBoardIdWithUser(id));

        User principal = (User) session.getAttribute("principal");

        if (principal != null) {
            Like likePS = likeRepository.findByBoardIdAndUserId(id, principal.getId());

            if (likePS == null) {
                likePS = new Like();
                likePS.setId(0);
                likePS.setCode(" ");
            }
            model.addAttribute("like", likePS);
        } else {
            Like likePS2 = new Like();
            likePS2.setId(0);
            likePS2.setCode(" ");
            model.addAttribute("like", likePS2);
        }
        return "board/detail";
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {

        User principal = (User) session.getAttribute("principal");

        if (principal == null) {
            throw new CustomApiException("????????? ?????? ???????????????.");
        }

        boardService.boardDelete(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "?????? ??????", null), HttpStatus.OK);
    }

    @GetMapping("/board/{id}/updateForm")
    public String boardupdateForm(@PathVariable int id, Model model) {
        Board boardPS = boardRepository.findById(id);
        User principal = (User) session.getAttribute("principal");

        if (principal == null) {
            throw new CustomException("????????? ?????? ???????????????.", HttpStatus.UNAUTHORIZED);
        }

        if (boardPS == null) {
            throw new CustomException("???????????? ???????????? ????????????");
        }

        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("????????? ?????? ????????? ????????????.");
        }

        model.addAttribute("board", boardPS);
        return "board/updateForm";

    }

    @PutMapping("/board/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {

        User principal = (User) session.getAttribute("principal");

        if (principal == null) {
            throw new CustomApiException("????????? ?????? ???????????????.", HttpStatus.UNAUTHORIZED);
        }
        if (boardUpdateReqDto.getTitle() == null || boardUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title??? ??????????????????");
        }
        if (boardUpdateReqDto.getContent() == null || boardUpdateReqDto.getContent().isEmpty()) {
            throw new CustomApiException("Content??? ??????????????????");
        }
        if (boardUpdateReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title??? ????????? 100??? ???????????? ?????????");
        }

        boardService.boardUpdate(id, principal.getId(), boardUpdateReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "?????? ??????", null), HttpStatus.OK);

    }
}