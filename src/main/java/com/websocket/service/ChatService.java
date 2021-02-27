package com.websocket.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.dto.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
	private final ObjectMapper objectMapper;
	private Map<String, ChatRoom> chatRooms;
	
	@PostConstruct
	private void init() {
		chatRooms = new LinkedHashMap<String, ChatRoom>();
	}
	
	public List<ChatRoom> findAllRoom(){
		return new ArrayList<ChatRoom>(chatRooms.values());
	}
	
	public ChatRoom findRoomById(String roomId) {
		return chatRooms.get(roomId);
	}
	
	public ChatRoom createRoom(String name) {
		String randomId = UUID.randomUUID().toString();
		ChatRoom chatRoom = ChatRoom.builder()
				.roomId(randomId)
				.name(name)
				.build();
		chatRooms.put(randomId, chatRoom);
		return chatRoom;
	}
	
	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
}