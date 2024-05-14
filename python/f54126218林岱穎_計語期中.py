# -*- coding: utf-8 -*-


foodmenu = ["親子丼", "厚切豬排丼", "唐揚雞丼", "月見起司牛丼", "蟹管丼飯", "炸蝦咖哩飯", "炙燒牛咖哩飯", "蔥香豬烏龍麵", "泡菜溫泉牛烏龍麵"]
group1 ={"type1:丼飯":{"1.親子丼", "2.厚切豬排丼", "3.唐揚雞丼", "4.月見起司牛丼", "5.蟹管丼飯"}}
group2 ={"type2:咖哩飯":{"6.炸蝦咖哩飯", "7.炙燒牛咖哩飯"}}
group3 ={"type3:烏龍麵":{"8.蔥香豬烏龍麵", "9.泡菜溫泉牛烏龍麵"}}

# key : 品項, value : 價格
menu = {1 : 130, 2 : 170, 3 : 150, 4 : 160, 5 : 160, 6 : 170, 7 : 190, 8 : 140, 9 : 170}
# 儲存餐點
meals = {}

price = 0
sum = 0
n = 1

while True:
  print("")
  print("歡迎光臨飢丼日式丼飯!,請問今天想吃什麼?")
  print(group1)
  print(group2)
  print(group3)

  while(1):
    prepare = int(input("請輸入[1to9]"))
    price = menu[prepare]

    print(f"您點的餐點為:{foodmenu[prepare-1]},{price}元")
    sum += price
    print("")

    # 客製化餐點
    print("以下為客製化餐點選項")
    choosearray1 = [" ", "飯/麵多", "飯/麵少", "飯/麵正常分量"]
    choosearray2 = [" ", "蔥多", "蔥少", "蔥正常分量"]
    choosearray3 = [" ", "重口味", "清淡口味", "正常口味"]
    choose1 = int(input("請問要1.飯/麵多，2.飯/麵少，還是3.飯/麵正常分量呢?"))
    choose2 = int(input("請問要1.蔥多，2.蔥少，還是3.蔥正常分量呢?"))
    choose3 = int(input("請問要1.重口味，2.清淡口味，還是3.正常口味呢?"))
    print(f"請確認餐點:{foodmenu[prepare-1]},{choosearray1[choose1]},{choosearray2[choose2]},{choosearray3[choose3]}")
    meals["第{n}餐"] = [foodmenu[prepare-1], choosearray1[choose1], choosearray2[choose2], choosearray3[choose3]]
    print("")
    conti = input(f"繼續點餐(y/n)")
    if( conti == "y"):
      n += 1
      continue
    break

  print("")
  print("1.內用")
  print("2.外帶")
  foodprepare = int(input("請問要內用還是外帶呢?"))
  if foodprepare == 1:
    table = int(input("要坐幾號桌呢(1號至8號)?"))
    print(f"先跟您結帳{sum}")

    foodsend = int(input("please input sendprice:"))
    if foodsend - sum > 0:
      print(f"找錢:{foodsend - sum}元")
      print("")
      print("謝謝光臨!!")
      break
    elif foodsend - sum < 0:
      print("交易失敗，投入的錢不足，已全數退款，請重新支付")
    elif foodsend - sum == 0:
      print("謝謝光臨!!")

  elif foodprepare == 2:
    print("1.需要")
    print("2.不用")
    tool = int(input("請問需要餐具嗎?"))
    print(f"先跟您結帳{sum}")

    foodsend = int(input("please input sendprice:"))
    if foodsend - sum > 0:
      print(f"找錢:{foodsend - sum}元")
      print("")
      print("謝謝光臨，請至取餐區等候!")
      break
    elif foodsend - sum < 0:
      print("交易失敗，投入的錢不足，已全數退款，請重新支付")
    elif foodsend - sum == 0:
      print("")
      print("謝謝光臨，請至取餐區等候!")
  file = open('example.txt', 'w')
  for key in meals:
    file.write(f'{key}:{meals[key]}\n')
  file.close()
  break
