/*
Must login before running the test
*/

// eslint-disable-next-line import/no-extraneous-dependencies
const { Builder, By, until } = require('selenium-webdriver');
// eslint-disable-next-line import/no-extraneous-dependencies
const chrome = require('selenium-webdriver/chrome');

// eslint-disable-next-line no-undef
jest.setTimeout(30000); // increase timeout for slow browsers

// eslint-disable-next-line no-undef
describe('CourseDifficultyRating UI (Selenium)', () => {
  let driver;

  // eslint-disable-next-line no-undef
  beforeAll(async () => {
    const options = new chrome.Options();
    options.addArguments('--headless');
    options.addArguments('--no-sandbox');
    options.addArguments('--disable-dev-shm-usage');

    driver = await new Builder().forBrowser('chrome').setChromeOptions(options).build();
    await driver.get('http://localhost:3000/FilterSelection'); // Ensure frontend is running
  });

  // eslint-disable-next-line no-undef
  afterAll(async () => {
    await driver.quit();
  });

  // eslint-disable-next-line no-undef
  test('filters courses, expands course details, and submits rating', async () => {
    // Step 1: Select COMPSCI in Subject Code section
    const subjectCodeHeader = await driver.findElement(By.xpath("//button[contains(text(), 'SUBJECT CODE')]"));
    await subjectCodeHeader.click();

    // Click on COMPSCI checkbox
    const compsciCheckbox = await driver.findElement(By.xpath("//div[contains(text(), 'COMPSCI')]/preceding-sibling::span[@role='checkbox']"));
    await compsciCheckbox.click();

    // Step 2: Select Level 1 in Course Level section
    const courseLevelHeader = await driver.findElement(By.xpath("//button[contains(text(), 'COURSE LEVEL')]"));
    await courseLevelHeader.click();

    // Click on Level 1 checkbox
    const level1Checkbox = await driver.findElement(By.xpath("//div[contains(text(), 'Level 1')]/preceding-sibling::span[@role='checkbox']"));
    await level1Checkbox.click();

    // Step 3: Click Apply Filters button
    const applyFiltersButton = await driver.findElement(By.className('apply-filters-btn'));
    await applyFiltersButton.click();

    // step 4: Wait for courses to load and expand COMPSCI 1XD3
    await driver.wait(until.elementLocated(By.id('COMPSCI 1XD3')), 10000);
    const course1XD3Button = await driver.findElement(By.id('COMPSCI 1XD3'));
    await course1XD3Button.click();

    // Step 5: Wait for course details to expand and click on Rating button
    await driver.wait(until.elementLocated(By.id('rating-btn')), 10000);
    const ratingButton = await driver.findElement(By.id('rating-btn'));
    await ratingButton.click();

    // Step 6: Wait for rating modal to appear and click on 3-star rating
    await driver.wait(until.elementLocated(By.className('rating-modal-content')), 5000);
    const starButtons = await driver.findElements(By.className('star-btn'));
    await starButtons[2].click(); // Click the 3rd star (index 2)

    // Step 7: Click Submit Rating button
    const submitRatingButton = await driver.findElement(By.className('submit-rating-btn'));
    await submitRatingButton.click();

    // Step 8: Verify thank you message appears
    const thankYouMessage = await driver.wait(
      until.elementLocated(By.className('thank-you-message')),
      5000
    );
    const messageText = await thankYouMessage.getText();
    // eslint-disable-next-line no-undef
    expect(messageText).toContain('Thank you for your rating');

    // Step 9: Verify that the rating statistics updated
    const currentRating = await driver.findElement(By.className('current-rating'));
    const ratingText = await currentRating.getText();
    // eslint-disable-next-line no-undef
    expect(ratingText).toContain('Medium');
  });
});
