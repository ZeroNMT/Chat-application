<h1><a id="Gii_thiu_ng_dng_0"></a>Giới thiệu ứng dụng</h1>
<p>Ứng dụng chat K3T sử dụng ngôn ngữ Java và nền tảng JavaFX để hoàn thành giao diện cho người dùng. Về phần mã hóa, ứng dụng sử dụng thư viện java.security và javax.crypto để hiện thực phần mật mã của ứng dụng.</p>
<p>Xây dựng được ứng dụng chat K3T theo mô hình Hybrid và trong đó kết hợp với mã hóa tin nhắn:</p>
<ul>
<li>Ứng dụng sử dụng tổng cộng 5 giải thuật: DES, AES, RSA, HMAC, DSA. Trong đó:<br>
- Giải thuật DES, AES, RSA được sử dụng mã hóa các tin nhắn.<br>
-  Giải thuật HMAC dùng để đảm bảo tính toàn vẹn và tính xác thực của mỗi tin nhắn.<br>
-  Giải thuật RSA, DSA dùng để trao đổi và phân phối khóa.</li>
<li>Tin nhắn có thể gửi đi với các dạng pdf, txt, png, jpg. Các tin nhắn này đều được mã hóa và sử dụng HMAC để đảm báo tính toàn vẹn dữ liệu</li>
<li>Quá trình mã hóa người dùng có 2 cách chọn key:<br>
-     Hệ thống sẽ tự động tạo key cho người dùng<br>
-      Người dùng có thể từ nhập key bằng cách chọn một file.</li>
<li>Khi người dùng thay đổi key hay giải thuật thì hệ thống sẽ tự động phân phối khóa đến các người dùng khác bằng cách sử dụng giải thuật RSA, DSA</li>
<li>Nếu file có dung lượng lớn thì người dùng có thể thấy thanh trạng thái đang mã hóa hay đang giải mã trên ứng dụng</li>
<li>Ứng dụng cho phép gửi file với dung lượng không quá 50 MB</li>
</ul>
<h1><a id="Hn_ch_18"></a>Hạn chế</h1>
<ul>
<li>Giải thuật mà người dùng chọn sẽ được áp dụng cho tất cả các user đang nói chuyện.</li>
<li>Vì dự án cũng khá lớn, nên code chưa được tối ưu, gây lãng phí tài nguyên. Khi gặp lỗi thì sẽ khó debug.</li>
<li>Chưa hiện thực được chức năng mã hóa tất cả tập tin hiện có trong thư mục</li>
</ul>
<h1><a id="Hng_pht_trin_thm_24"></a>Hướng phát triển thêm</h1>
<ul>
<li>Kết nối ứng dụng  với database</li>
<li>Hiện thực phần đăng nhập, đăng ký cho ứng dụng chat. Khi đó, sử dụng giải thuật để mã hóa các mật khẩu của người dùng. Đảm bảo thông tin người dùng không bị mất.</li>
<li>Hiện thực việc user có thể chọn mỗi giải thuật mã hóa đối với mỗi người dùng để tăng tính bảo mật của ứng dụng chat.</li>
<li>Tăng kích thước file mã hóa bằng cách chia file thành nhiểu mảng byte hơn thay vì chỉ dùng một mảng byte lưu file</li>
<li>Cho phép mã hóa nhiều định dạng hơn nữa như mp3, video,…</li>
</ul>
<h1><a id="Mt_s_hnh_nh_32"></a>Một số hình ảnh</h1>

![](C:\Users\ZERO.NMT\Desktop\Untitled.png)