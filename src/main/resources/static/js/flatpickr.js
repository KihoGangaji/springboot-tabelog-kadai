let maxDate = new Date();
 maxDate = maxDate.setMonth(maxDate.getMonth() + 3);
 
  // flatpickrの設定
        flatpickr("#fromReserveDateTime", {
            locale: 'ja',           // 日本語に設定
            minDate: 'today',       // 今日以降の日付のみ選択可能
            maxDate: maxDate,       // 3か月後まで選択可能
            dateFormat: "Y-m-d HH:mm",    // 日付フォーマットを "YYYY-MM-DD" に設定
            defaultDate: new Date() // デフォルトで今日の日付を設定
 });