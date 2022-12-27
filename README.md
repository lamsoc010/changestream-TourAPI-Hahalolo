
Luồng hoạt động của demo send notification:
****Bài toán: Khi 1 priceOpen được insert vào db(Tức là có giá tour được thêm mới hoặc cập nhập) thì gửi thông báo đến cho toàn bộ user được biết về giá của tour vừa được thêm đó.

****Giải quyết:

	1. Lắng nghe sự kiện của priceTour và dateOpen
	Không cần biết 2 thằng này được lắng nghe sự kiện nào, chỉ quan tâm đến việc là khi sự kiện được lắng nghe, thì thằng priceOpen được insert vào database thì sẽ gửi thông báo cho client 
	==> Vì vậy sẽ quan tâm đến hàm function insertPriceOpen trong PriceOpenService.
	
	2. Lấy được listUser theo topic(topic ở đây là chủ đề mà user quan tâm, ví dụ ở đây user đang quan tâm đến tour, thì topic sẽ là "tour")
		//Input: topic
		//Output: listUser => List<ObjectId> listUserId
	function getListUserByTopic trong UserService	
	
	3. Lấy được listTokenDevice theo listUserId
		//Input: listUserId
		//Output: List<String> listToken
	
	4. Từ 2 và 3 ta có thể gộp lại thành function getListTokenByTopic 
	function getListTokenByTopic trong TokenUserService
	
	5. Có được listToken rồi thì tiến hành gửi thông báo cho client theo listToken: 
	function sendNotificationToAlLUserByTopic trong PriceOpenService