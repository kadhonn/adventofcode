pub fn day4_1(str: &str) {
    let mut count = 0;
    for line in str.split("\r\n") {
        if line.len() == 0 {
            continue;
        }
        let mut r = line.split(",");
        let mut r1s = r.next().unwrap().split("-");
        let mut r2s = r.next().unwrap().split("-");

        let s1: i32 = r1s.next().unwrap().parse().unwrap();
        let e1: i32 = r1s.next().unwrap().parse().unwrap();
        let s2: i32 = r2s.next().unwrap().parse().unwrap();
        let e2: i32 = r2s.next().unwrap().parse().unwrap();

        if (s1 <= s2 && e1 >= e2) || (s1 >= s2 && e1 <= e2) {
            count += 1;
        }
    }

    println!("{}", count);
}

pub fn day4_2(str: &str) {
    let mut count = 0;
    for line in str.split("\r\n") {
        if line.len() == 0 {
            continue;
        }
        let mut r = line.split(",");
        let mut r1s = r.next().unwrap().split("-");
        let mut r2s = r.next().unwrap().split("-");

        let s1: i32 = r1s.next().unwrap().parse().unwrap();
        let e1: i32 = r1s.next().unwrap().parse().unwrap();
        let s2: i32 = r2s.next().unwrap().parse().unwrap();
        let e2: i32 = r2s.next().unwrap().parse().unwrap();

        if (s1 <= s2 && e1 >= e2) || (s1 >= s2 && e1 <= e2) || (s1 <= s2 && s2 <= e1) || (s1 <= e2 && e2 <= e1) {
            count += 1;
        }
    }

    println!("{}", count);
}