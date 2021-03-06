﻿/*
 * @(#)Repository.java        1.8.0_191 2019/03/27
 * 
 * Copyright (c) 2019 Youngbae Son
 * ComputerScience, ProgrammingLanguage, Java, Busan, KOREA
 * All rights reserved.
 * */

package com.yb.numberbaseballgame;

import java.util.Scanner;

/*
 * 3. 컴퓨터 난수 생성 : createNumber()
 * 4. 게임 가이드 메세지 출력 : guideMessage()
 * 5. 사용자의 값 입력 : input()
 * 6. 1~9이외 값, 중복 검사확인 : userNumCheck()
 * 7. 스트라이크, 볼인지 검사 : playGame()
 * 8. X 스트라이크 X 볼 : printBallCount()
 * 9. run() : 3 -> 4 -> 5 -> 6-> 7-> 8->  9.계속  or 종료
 * 
 * @author 손영배
 * */

public class Repository implements Service {

	/*
	 * 컴퓨터,사용자, 볼 카운트를 담는 객성을 생성
	 * 
	 * 라이프사이클 3 -> 4 -> 5 -> 6-> 7-> 8-> 9.계속 or 종료
	 *
	 * 게임을 계속 하게 된다면 while 실행 게임을 종료 하게된다면 while 탈출
	 *
	 */

	public void run() {

		/* 컴퓨터 숫자를 담을 객체 생성 */
		Number comNumber = new Number();

		/* 사용자 숫자를 담을 객체 생성 */
		Number userNumber = new Number();

		/* 볼 카운트를 담을 객체 생성 */
		Ballcount ballCnt = new Ballcount();

		/* 사용자 입력을 받는 String 타입 변수 */
		String inputNum;

		createNumber(comNumber);

		while (true) {

			guideMessage("ready");

			inputNum = input();

			if (!userNumCheck(userNumber, inputNum)) {
				continue;
			}

			ballCnt = playGame(comNumber, userNumber, ballCnt);

			printBallCount(ballCnt);

			if (ballCnt.getStrike() == 3) {
				guideMessage("correct");
				break;
			}
		}
	}

	/*
	 * Computer 숫자를 생성하는 함수
	 * 
	 * @param comNumber[0],[1],[2] 자리에 1~9자리 숫자 생성
	 * 
	 * Math.random() 함수는 0.0 ~ 1.0 값을 생성하기 때문에 *10를 증가 후 범위를 0~10 만들어 0, 10만 제외한
	 * 1~9의 수 를 comNumber[0],[1],[2] 자리에 저장
	 * 
	 */
	public void createNumber(Number comNumber) {

		int index = 0;
		int value = 0;
		boolean[] visit = new boolean[10];

		while (index < 3) {

			value = (int) (Math.random() * 10);

			if (value == 0 || value == 10 || visit[value]) {
				continue;
			} else {
				comNumber.setNumber(index, value);
				visit[value] = true;
				index++;
			}
		}

	}

	/*
	 * @param message가 "ready"이면 "숫자를 입력해주세요 : " 출력 "correct"이면
	 * "3개의 숫자를 모두 맞히셨습니다! 게임종료" "게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요"
	 */
	public void guideMessage(String message) {

		if (message.equals("ready")) {

			System.out.print("숫자를 입력해주세요 : ");

		} else if (message.equals("correct")) {

			System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임종료");
			System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요");
		}
	}

	/*
	 * Scanner를 이용하여 사용자 숫자를 String input에 저장 한 후
	 * 
	 * @return input
	 */
	public String input() {

		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();

		return input;
	}

	/*
	 * 입력 받은 사용자 숫자 @param inputNum에서 발생 할 수 있는 예외를 검사한다.
	 * 
	 * 1. 입력 받은 숫자가 3자리가 아닐 경우 @return false 2. 입력 받은 숫자가 1~9가 범위, 중복 입력 시 @ return
	 * false
	 * 
	 * 1,2 검사를 통과한다면 @parm userNumber[0],[1],[2] 자리에 저장
	 * 
	 * 3자리 입력이 끝나며 @return true
	 */
	public boolean userNumCheck(Number userNumber, String inputNum) {

		String[] tmp = inputNum.split("");
		boolean[] visit = new boolean[10];

		if (inputNum.length() != 3) {
			return false;
		}

		for (int i = 0; i < 3; i++) {

			int value = ((int) tmp[i].charAt(0)) - 48;

			if (value < 1 || value > 9 || visit[value]) {
				return false;
			}

			userNumber.setNumber(i, Integer.parseInt(tmp[i]));
			visit[value] = true;
		}

		return true;
	}

	/*
	 * @param comNumber[0],[1],[2] 숫자와 @param userNumber[0],[1],[2]를 반복 비교해서 같은 자리에
	 * 같은 수가 있다면 st_cnt 증가 같은 수가 다른 자리에 있다면 ball_cnt ball 증가
	 * 
	 * result strike에 st_cnt 저장 result ball에 ball_cnt 저장
	 * 
	 * @return result
	 */
	public Ballcount playGame(Number comNumber, Number userNumber, Ballcount ballCnt) {

		Ballcount result = new Ballcount();

		int st_cnt = 0;
		int ball_cnt = 0;

		for (int i = 0; i < 3; i++) {
			if (comNumber.getNumber(i) == userNumber.getNumber(i)) {
				st_cnt++;
			} else {
				for (int j = 0; j < 3; j++) {
					if (i != j && comNumber.getNumber(i) == userNumber.getNumber(j))
						ball_cnt++;
				}
			}
		}

		result.setStrike(st_cnt);
		result.setBall(ball_cnt);

		return result;

	}

	/*
	 * @param ballCnt에 저장되어 있는 strike와 ball 갯수 출력 둘 다 없다면 "낫싱" 출력
	 */
	public void printBallCount(Ballcount ballCnt) {

		int strike = ballCnt.getStrike();
		int ball = ballCnt.getBall();

		if (strike > 0) {
			System.out.print(strike + "스트라이크 ");
		}

		if (ball > 0) {
			System.out.print(ball + "볼 ");
		}

		if (strike == 0 && ball == 0) {
			System.out.print("낫싱");
		}

		System.out.println();
	}

}
