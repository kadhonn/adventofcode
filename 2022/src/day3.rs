pub fn day3_1(str: &str) {
    let mut prio = 0;
    'rucksacks: for rucksack in str.split("\r\n") {
        let (first, second) = rucksack.split_at(rucksack.len() / 2);
        for ch in first.chars() {
            if second.contains(ch) {
                if 'a' <= ch && ch <= 'z' {
                    prio += ch as i32 - 'a' as i32 + 1;
                } else {
                    prio += ch as i32 - 'A' as i32 + 27;
                }
                continue 'rucksacks;
            }
        }
    }
    println!("{}", prio);
}

pub fn day3_2(str: &str) {
    let mut prio = 0;
    let mut rucksacks = str.split("\r\n").enumerate();
    'rucksacks: loop {
        let check = rucksacks.next();
        if check.is_none() {
            break;
        };
        let rucksack1 = check.unwrap().1;
        let rucksack2 = rucksacks.next().unwrap().1;
        let rucksack3 = rucksacks.next().unwrap().1;

        for ch in rucksack1.chars() {
            if rucksack2.contains(ch) && rucksack3.contains(ch) {
                prio += calc_prio(ch);
                continue 'rucksacks;
            }
        }
    }

    println!("{}", prio);
}

fn calc_prio(ch: char) -> i32 {
    if 'a' <= ch && ch <= 'z' {
        ch as i32 - 'a' as i32 + 1
    } else {
        ch as i32 - 'A' as i32 + 27
    }
}