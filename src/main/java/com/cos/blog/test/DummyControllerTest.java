package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.Board;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	private String daleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		}catch(Exception e){
			return "삭제할 회원이 없습니다";
		}
		return "회원삭제가 완료되었습니다";
	}
	
	@PutMapping("dummy/user/{id}")
	private User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정할 대상이 없습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		userRepository.save(user);
		
		return user;
	}
	
	@PostMapping("/dummy/join")
	public User join(User user) {
		user.setRole(RoleType.USER);
		userRepository.save(user);
		System.out.println("회원가입이 완료되었습니다");
		return user;		
	}
	
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
//		// 없을 때 Null 반환함.
//		User user = userRepository.findById(id).get();
		
		// 예외와 메시지를 반환함.
		User user =userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("id= "+ id);
			}
		});
		
//		// 예외와 메시지를 반환함. --> 람다식표기법
//		User user =userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("id= "+ id);
//		});

//		// 없을 때 Null Object 반환함.
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			@Override
//			public User get() {
//				return new User();
//			}
//		});
		return user;
		
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}

	@GetMapping("dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		List<User> users = pagingUser.getContent();
		return users;
	}

	@GetMapping("/dummy/boards")
	public List<Board> listBoard(){
		return boardRepository.findAll();
	}


}
