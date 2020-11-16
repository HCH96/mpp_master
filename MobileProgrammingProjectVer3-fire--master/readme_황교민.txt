[ThirdPage.java]
1. 주요 기능 : 
-> firstpage에서 선택한 날짜의 메모를 저장한다. 만약에 firstpage의 날짜가 변경되면 그 날짜에 저장된 메모의 내용이 표시된다.
-> 저장버튼 / 초기화 버튼 / 메모내용 입력칸 3가지로 구성된다.
-> 저장버튼은 메모의 내용을 저장할 수 있다.
-> 초기화 버튼은 메모의 내용을 초기화 시킨다. 단 이때, 메모의 내용은 초기화상태로 저장되지 않는다.
-> 메모 내용 입력 : 뷰의 중앙부분부터 작성이 가능하다.
2. 구현
-> 1. onCreateView : 초기 화면의 상태를 지정한다.
        	- content : 내용을 입력하는 editText를 가져옴.
        	- saveButton : 저장 버튼을 가져옴.
        	- clearButton : 초기화 버튼을 가져옴.
        	- clearButton.setOnClickListener : 초기화 버튼 클릭시, 내용 입력칸의 text가 "" 으로 변경됨
        	- saveButton.setOnClickListener : content의 내용을 firebase db에 저장함.
-> 2. changeMemo : firstpage에서 날짜가 변경될 때마다 메모의 내용이 바뀌도록 하기 위함.
	- firstpage의 날짜가 변경될 때 반응해야 하기 때문에 static 메소드로 지정함.
	- 바뀐 날짜 정보를 changeMemo의 매개변수로 넣고, firstpage에서 실행한다. 
	- 바뀐 날짜에 해당하는 memo내용이 있으면 content에 표시해준다. -> isIn변수로 있는지 없는지 상태를 저장한다.
	- 만약 내용이 없으면 content를 "" 으로 표시한다.
* thirdpage의 전체적인 UI는 김지원 학우의 도움을 받았습니다.


[Calender.java]
1. 주요 기능
-> materialCalendarView를 이용하여 달력을 표시함. 할일 추가 버튼을 클릭하여 할일 내용 및 시작/종료 날짜를 지정할 수 있다. 지정 후에는 달력에
일정이 있는 날에는 빨간 점이 표시된다. 달력 아래에는 클릭한 당일의 일정을 보여준다. 체크박스를 클릭하여 일정을 삭제할 수도 있다.
2. 구현
->1. onCreate : 초기 화면의 상태를 지정한다.
	- btn : 할일 추가를 할 수 있는 버튼이다.
	- calender : materialCalendarView를 저장함.
	- showTodos : 달력 아래에 할일을 상세하게 보여줄 layout임.
	- calender.addDecorators : 달력에 오늘(당일) 날짜를 보여주기 위해서 생성함.
	- calender.setOnDateChangedListener : 사용자가 다른 날짜를 클릭했을 경우, 해당 날짜의 할일을 보여주기 위해서 생성.
		- database.child("monthly").addValueEventListener : db에 접근하면서 화면에 빨간 점을 표시하고, 유저가 클릭한 날짜의 할일을 동적으로 표시
	- btn.setOnClickListener : 해당 요일의 할일을 추가하기 위해서 생성함. 클릭시 monthlyPopup페이지가 나타남. 이때, 유저가 선택한
			        날짜 정보를 전달하기 위해서 intent를 사용함
-> 2. setting : 초기에 화면상 일정 표시를 하기 위해서 사용. onCreate에서는 calender의 날짜가 변경되면 호출되는 함수이기 때문에, 처음 생성되었을
	      당시에는 날짜가 변경되지 않았으므로, 따로 함수를 제작하여 초기 화면 상태를 지정함.
	- onCreate의 calender.setOnDateChangedListener 함수 내의 내용과 동일함.
* 전체적인 UI는 김지원 학우의 도움을 받았습니다.

[monthlyPopup.java]
1. 주요 기능
-> 할일을 등록하기 위한 팝업 페이지. 시작/종료 날짜, 내용을 지정가능함.
2. 구현
	- onCreate : register(할일 등록) / cancel(취소 버튼) / selectDate(종료 날짜 선택 버튼)을 생성함. intent를 받아오는데, 이때 10 미만의 숫자는
		   "0"을 추가적으로 붙임(ex. 1월 -> 01월)
	- btnS.setOnClickListener : 달력을 보여주어 선택 가능함.
	- btnR.setOnClickListener : 할일을 저장함.
	- btnC.setOnClickListener : 다시 화면으로 돌아감.

* 전체적인 UI는 김지원 학우의 도움을 받았습니다.
