pub fn day2_1(str: &str) {
    let mut sum = 0;
    for round in str.split("\r\n") {
        if round.len() == 0 {
            continue;
        }
        let enemy = round.chars().nth(0).unwrap() as u32 - 'A' as u32;
        let my = round.chars().nth(2).unwrap() as u32 - 'X' as u32;

        sum += my + 1;
        sum += if enemy == my {
            3
        } else if enemy == 0 && my == 2 {
            0
        } else if enemy == 2 && my == 0 {
            6
        } else if enemy < my {
            6
        } else {
            0
        };
    }
    println!("{}", sum);
}

pub fn day2_2(str: &str) {
    let mut sum = 0;
    for round in str.split("\r\n") {
        if round.len() == 0 {
            continue;
        }
        let enemy = round.chars().nth(0).unwrap() as u32 - 'A' as u32;
        let should = round.chars().nth(2).unwrap();

        let my = ((enemy as i32 + match should {
            'X' => -1,
            'Y' => 0,
            'Z' => 1,
            _ => panic!("should was invalid value: {}", should)
        } + 3) % 3) as u32;

        sum += my + 1;
        sum += if enemy == my {
            3
        } else if enemy == 0 && my == 2 {
            0
        } else if enemy == 2 && my == 0 {
            6
        } else if enemy < my {
            6
        } else {
            0
        };
    }
    println!("{}", sum);
}